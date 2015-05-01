package com.FCI.SWE.Models;
import com.FCI.SWE.ServicesModels.UserEntity;

public abstract class Post {
	
	public UserEntity owner ;
	public String content ;
	public int likes ;
	public Long id ;
	
	public Post(){

	}
	
	public abstract Boolean savePost();

}