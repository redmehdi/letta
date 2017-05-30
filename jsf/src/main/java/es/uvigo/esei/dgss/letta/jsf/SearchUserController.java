package es.uvigo.esei.dgss.letta.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.State;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.UserEJB;

@ViewScoped
@ManagedBean(name = "searchUserController")
public class SearchUserController {
	
	private int pageIndex = 0;
	private int pages = 0;
	private int currentPage = 1;

	private List<String> pagesLinks = new ArrayList<>();
	
	private String keyword;
	
	@Inject 
	private UserEJB userEJB;
	
	private List<User> users;
	
	private String name;
	
	private String country;
	
	private byte[] photo;
	
	@PostConstruct
	public void init() { 
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getRequestParameterMap()
				.get("keyword") != null) {
			String srch_terms = (String) facesContext.getExternalContext()
					.getRequestParameterMap().get("keyword");

			int page = Integer.parseInt(facesContext.getExternalContext()
					.getRequestParameterMap().get("page"));
			
	}
		
		if(this.keyword != null) {
			
			this.users = userEJB.searchUser(keyword);
			
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	

}
