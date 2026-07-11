package vinix.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class RoleDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
  private Long id;
  private String roleName;
  
}
