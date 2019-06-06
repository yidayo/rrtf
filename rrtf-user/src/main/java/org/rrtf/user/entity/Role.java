package org.rrtf.user.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author yubing
 *
 */
@Entity
public class Role {
	@Id
	@GeneratedValue
	private int roleId;
	private String roleName;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RolePer", joinColumns = { @JoinColumn(name = "roleId") },
	inverseJoinColumns = {@JoinColumn(name = "pId") })
	private List<Permission> permissions;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", permissions=" + permissions + "]";
	}
}
