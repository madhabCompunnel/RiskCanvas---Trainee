package riskcanvas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Password {
	String oldPassword;
	String newPassword;
	
	@JsonProperty("oldpw")
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	@JsonProperty("newpw")
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
