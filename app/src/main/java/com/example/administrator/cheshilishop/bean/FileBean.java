package com.example.administrator.cheshilishop.bean;


import com.example.administrator.cheshilishop.bean.tree.TreeNodeId;
import com.example.administrator.cheshilishop.bean.tree.TreeNodeLabel;
import com.example.administrator.cheshilishop.bean.tree.TreeNodePid;

public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private String mobile;

	public FileBean(int _id, int parentId, String name)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
	}

}
