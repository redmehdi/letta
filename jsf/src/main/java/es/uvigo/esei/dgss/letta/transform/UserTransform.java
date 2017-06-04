package es.uvigo.esei.dgss.letta.transform;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;

import es.uvigo.esei.dgss.letta.bean.UserBean;
import es.uvigo.esei.dgss.letta.domain.entities.User;

public class UserTransform {

	public UserTransform() {
		super();
	}

	protected List<UserBean> transformUsers(List<User> friends, final String loginUser) {
		List<UserBean> userBeans = new ArrayList<UserBean>();
		for (User list : friends) {
			if (!list.getLogin().equals(loginUser)) {
	
				UserBean userBean = new UserBean();
				userBean.setCity(list.getCity());
				userBean.setName(list.getCompleteName());
				userBean.setLogin(list.getLogin());
				userBean.setEmail(list.getEmail());
	
				if (list.getImage() == null) {
					userBean.setPicture(new DefaultStreamedContent());
				} else {
					userBean.setPicture(new DefaultStreamedContent(new ByteArrayInputStream(list.getImage())));
				}
	
				userBeans.add(userBean);
			}
	
		}
		return userBeans;
	}
	
	protected UserBean transformUser(User friend, final String loginUser) {
		if (!friend.getLogin().equals(loginUser)) {

			UserBean userBean = new UserBean();
			userBean.setCity(friend.getCity());
			userBean.setName(friend.getCompleteName());
			userBean.setLogin(friend.getLogin());
			userBean.setEmail(friend.getEmail());

			if (friend.getImage() == null) {
				userBean.setPicture(new DefaultStreamedContent());
			} else {
				userBean.setPicture(new DefaultStreamedContent(new ByteArrayInputStream(friend.getImage())));
			}

			return userBean;
		}
		return null;
	}

}