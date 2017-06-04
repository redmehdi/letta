package es.uvigo.esei.dgss.letta.bean;

import java.io.IOException;
import org.primefaces.model.StreamedContent;

public class UserBean {
	
	private String login;
	private String name;
	private String city;
	private String email;
	private StreamedContent picture;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public StreamedContent getPicture() throws IOException{
		return picture;
	}
	public void setPicture(StreamedContent picture) {
		this.picture = picture;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
