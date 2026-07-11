package vinix.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String name;
    private String email;
    private Set<RoleDTO> roles = new HashSet<>();
    // sem password aqui 
}