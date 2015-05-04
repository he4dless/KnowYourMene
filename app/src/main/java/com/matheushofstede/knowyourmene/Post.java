package com.matheushofstede.knowyourmene;

public class Post {
	
	public String link;

	public String tag1;
	public String tag2;
	public String tag3;
	
	public Post(){
	}
	public Post(String link, String tag1, String tag2, String tag3){
		
		this.link = link;

		this.tag1 = tag1;
		this.tag2 = tag2;
		this.tag3 = tag3;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public String getTag2() {
		return tag2;
	}
	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}
	public String getTag3() {
		return tag3;
	}
	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}
	
	
	

	

}