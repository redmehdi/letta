package es.uvigo.esei.dgss.letta.jsf;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.bean.UserBean;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.transform.UserTransform;

@ViewScoped
@ManagedBean(name = "searchUserController")
public class SearchUserController extends UserTransform implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1878250668939516230L;
	private int pageIndex = 0;
	private int pages = 0;
	private int currentPage = 1;
	private List<String> pagesLinks = new ArrayList<>();
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	
	private String keyword;
	
	@Inject 
	private UserEJB userEJB;
	
	@Inject Principal currentUserPrincipal;

	private List<UserBean> users = new ArrayList<>();
	

	@PostConstruct
	public void init() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getRequestParameterMap().get("keyword") != null) {
			String keyword_term = (String) facesContext.getExternalContext().getRequestParameterMap()
					.get("keyword");

			if(keyword_term != null && !keyword_term.equals("")) {
				this.keyword = keyword_term;
			}
			this.users = transformUsers(userEJB.searchUser(keyword), this.currentUserPrincipal.getName());
		}


	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public List<UserBean> getUsers() {
		return users;
	}
	
	public void setUsers(List<UserBean> userBeans) {
		this.users = userBeans;
	}
	
	public List<UserBean> getUser(){
		
		return transformUsers(userEJB.searchUser(keyword), this.currentUserPrincipal.getName());
	}

	public List<String> getPagesLinks() {
		return pagesLinks;
	}

	public void setPagesLinks(List<String> pagesLinks) {
		this.pagesLinks = pagesLinks;
	}
	

}
