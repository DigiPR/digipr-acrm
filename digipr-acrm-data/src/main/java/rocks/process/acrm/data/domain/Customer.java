package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@Email(message = "Please provide a valid e-mail.")
	@NotEmpty(message = "Please provide an e-mail.")
	private String email;
	private String mobile;
	@ManyToOne
	@JsonIgnore
	private Agent agent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
