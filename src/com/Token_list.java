package com;

public class Token_list {
	public String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String value;
	public Token_list pnext;
	public Token_list()
	{
		this.key="-1";
	}
	public void add(String key,String value)
	{
		this.value=value;
		this.key=key;
	}
	public String get()
	{
		return "<"+this.key+","+this.value+">";
	}
}

class Symbol_list
{
	private String str;
	private int postion;
	
	public int getPostion() {
		return postion;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
	public Symbol_list pdown;
	
	public Symbol_list()
	{
		this.postion=-1;
	}
	public void add(int pos,String value)
	{
		this.postion=pos;
		this.str=value;
	}
	public String get()
	{
		return "("+this.postion+","+this.str+")";
	}
	public String getstr()
	{
		return this.str;
	}
	public int getpos()
	{
		return this.postion;
	}
}
