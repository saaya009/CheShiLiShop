package com.example.administrator.cheshilishop.photochoose;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Handler;


import com.example.administrator.cheshilishop.utils.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


/**
 * 裁剪处理
 *
 */
public class CropImage
{
	
	public boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    public boolean mSaving; // Whether the "save" button is already clicked.
    public HighlightView mCrop;
    
	private Context mContext;
	private Handler mHandler;
	private CropImageView mImageView;
	private Bitmap mBitmap;
	
	public CropImage(Context context, CropImageView imageView, Handler handler)
	{
		mContext = context;
		mImageView = imageView;
		mImageView.setCropImage(this);
		mHandler = handler;
	}
	
	/**
	 * 图片裁剪
	 */
	public void crop(Bitmap bm)
	{
		mBitmap = bm;
		startFaceDetection();
	}
	
	public void startRotate(float d) {
        if (((Activity)mContext).isFinishing()) {
            return;
        }
        final float degrees = d;
        showProgressDialog("请稍等", new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                mHandler.post(new Runnable() {
                    public void run() {
                        try{
	                        Matrix m = new Matrix();
	                		m.setRotate(degrees);
	                		Bitmap tb = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),m,false);
	                		mBitmap = tb;
	                		mImageView.resetView(tb);
	                		if (mImageView.mHighlightViews.size() > 0) {
	                            mCrop = mImageView.mHighlightViews.get(0);
	                            mCrop.setFocus(true);
	                        }
                        }catch (Exception e) {
							// TODO: handle exception
						}
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                mRunFaceDetection.run();
            }
        }, mHandler);
    }
	
	private void startFaceDetection() {
        if (((Activity)mContext).isFinishing()) {
            return;
        }
        showProgressDialog("请稍等", new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        if (b != mBitmap && b != null) {
                            mImageView.setImageBitmapResetBase(b, true);
                            mBitmap.recycle();
                            mBitmap = b;
                        }
                        if (mImageView.getScale() == 1.0f) {
                            mImageView.center(true, true);
                        }
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mRunFaceDetection.run();
            }
        }, mHandler);
    }

	/**
	 * 裁剪并保存
	 * @return
	 */
	public Bitmap cropAndSave(){
		final Bitmap bmp = onSaveClicked(mBitmap);
		mImageView.mHighlightViews.clear();
		return bmp;
	}
	/**
	 * 裁剪并保存
	 * @return
	 */
	public Bitmap cropAndSave(Bitmap bm)
	{
		final Bitmap bmp = onSaveClicked(bm);
		mImageView.mHighlightViews.clear();
		return bmp;
	}
	
	/**
	 * 取消裁剪
	 */
	public void cropCancel()
	{
		mImageView.mHighlightViews.clear();
		mImageView.invalidate();
	}
	
    private Bitmap onSaveClicked(Bitmap bm) {
        // CR: TODO!
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return bm;

        if (mCrop == null) {
            return bm;
        }

        mSaving = true;

        Rect r = mCrop.getCropRect();
        int width = r.width(); // CR: final == happy panda!
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
//        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        {
//            Canvas canvas = new Canvas(croppedImage);
//            Rect dstRect = new Rect(0, 0, width, height);
//            canvas.drawBitmap(bm, r, dstRect, null);
//        }

//        Rect dr = mCrop.mDrawRect;
//        int width = 70;
//        int height = 70;
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(bm, r, dstRect, null);
        }
        
        return croppedImage;
    }

    public String saveToLocal(Bitmap bm)
    {
    	String path = Config.IMAGE_PATH+"/"+ String.valueOf((new Date())
				.getTime()) + ".png";
        File file = new File(Config.IMAGE_PATH);
        if(!file.exists()){
            file.mkdirs();
        }

    	try
		{
			FileOutputStream fos = new FileOutputStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 75, baos);
			int options = 100;
	         if(baos.toByteArray().length / 1024<50){
	        	 bm.compress(CompressFormat.JPEG, 75, fos);
	         }else{
	        	 while ( baos.toByteArray().length / 1024 > 50 &&options>50) {  //循环判断如果压缩后图片是否大于50kb,大于继续压缩         
		             baos.reset();//重置baos即清空baos  
		             bm.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		            options -=10;
		         } 
	        	 ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
	        	 BitmapFactory.Options opts = new BitmapFactory.Options();
	       	 	int size =  baos.toByteArray().length;
	              if (size <= 50 * 1024)
	              { // 0-50k
	            	  opts.inSampleSize = 1;
	              }
	              else if (size <= 100 * 1024)
	              { // 51K-100k
	            	  opts.inSampleSize = 2;
	              }
	              else if (size <= 400 * 1024)
	              { // 101K-400M
	            	  opts.inSampleSize = 3;
	              }
	              else if (size <= 800 * 1024)
	              { // 401k-800k
	            	  opts.inSampleSize = 4;
	              }
	              else
	              {
	                  opts.inSampleSize = 8;
	              }
	        	 
	        	 
	        	 Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);//把ByteArrayInputStream数据生成图片
	             //把压缩后的图片写入SD卡
	             bitmap.compress(CompressFormat.JPEG, 75, fos);
	         }
	         
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return path;
    }

    private void showProgressDialog(String msg, Runnable job, Handler handler)
    {
//    	final ProgressDialog progress = ProgressDialog.show(mContext, null, msg);
//    	new Thread(new BackgroundJob(progress, job, handler)).start();
    	new Thread(new BackgroundJob(msg, job, handler)).start();
    }
    
    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, false, true);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // CR: sentences!
            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, false, true);
            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth(); // CR: F => f (or change
                                                      // all f to F).
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
//                    if (mNumFaces > 0) {
////                        for (int i = 0; i < mNumFaces; i++) {
//                        for (int i = 0; i < 1; i++) {
//                            handleFace(mFaces[i]);
//                        }
//                    } else {
                        makeDefault();
//                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() > 0) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        // CR: no need for the variable t. just do
                        // Toast.makeText(...).show().
//                        Toast t = Toast.makeText(mContext, R.string.multiface_crop_help, Toast.LENGTH_SHORT);
//                        t.show();
                    }
                }
            });
        }
    };
	
	class BackgroundJob implements Runnable
    {
//    	private ProgressDialog mProgress;
    	private String message;
    	private Runnable mJob;
    	private Handler mHandler;
//    	public BackgroundJob(ProgressDialog progress, Runnable job, Handler handler)
//    	{
//    		mProgress = progress;
//    		mJob = job;
//    		mHandler = handler;
//    	}
    	public BackgroundJob(String m, Runnable job, Handler handler)
    	{
    		message = m;
    		mJob = job;
    		mHandler = handler;
//    		mProgress = new ProgressDialog(mContext);
//    		mProgress.setMessage(message);
//    		mProgress.show();
    	}
    	public void run()
    	{
    		final CountDownLatch latch = new CountDownLatch(1);
    		mHandler.post(new Runnable() {
                public void run() {
                    try{
                    	mHandler.sendMessage(mHandler.obtainMessage(CropImageActivity.SHOW_PROGRESS));
//                    	if (mProgress != null && !mProgress.isShowing())
//    					{
//                    		mProgress.show();
//    					}
                    }catch (Exception e) {
						// TODO: handle exception
					}
                    
                    latch.countDown();
                }
            });
    		 try {
                 latch.await();
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
    		try 
    		{
    			mJob.run();
    		}
    		finally
    		{
    			mHandler.sendMessage(mHandler.obtainMessage(CropImageActivity.REMOVE_PROGRESS));
//    			mHandler.post(new Runnable()
//    			{
//    				public void run()
//    				{
//    					if (mProgress != null && mProgress.isShowing())
//    					{
//    						mProgress.dismiss();
//    						mProgress = null;
//    					}
//    				}
//    			});
    		}
    	}
    }
}
