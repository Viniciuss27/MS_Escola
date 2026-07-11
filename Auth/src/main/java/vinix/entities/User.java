package vinix.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter 
@EqualsAndHashCode(of = "id") 
@Entity @Table(name = "tb_user")
public class User implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id @Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Setter
	private String name;
	
	@Column(unique = true) 
	@Setter
	private String email;

	@Setter
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	// recebe do JSON e não aparece na resposta
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable( name = "tb_user_role",
	    joinColumns = @JoinColumn(name = "user_id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User(Long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream() // converte para stream
				.map(x -> new SimpleGrantedAuthority
				// cada stream é convertido para SimpleGrantedAuthority
				(x.getRoleName()))
				// consegue pegar por ser uma implementação do GrantedAuthority
				.collect(Collectors.toList());

		/* retorna as roles convertidas para o Spring Security */
	}

	@Override
	public String getUsername() {
		return email; /* retorna o email como identificador do usuário */
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; /* conta não expirada */
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; /* conta não bloqueada */
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; /* senha não expirada */
	}

	@Override
	public boolean isEnabled() {
		return true; /* conta ativa */
	}
}