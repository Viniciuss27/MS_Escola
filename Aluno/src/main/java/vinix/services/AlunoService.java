package vinix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import vinix.dto.CriarUsuarioDTO;
import vinix.entities.Aluno;
import vinix.feign.AuthFeignClient;
import vinix.repositories.AlunoRepository;
import vinix.services.exceptions.DatabaseException;
import vinix.services.exceptions.ResourceNotFoundException;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository rep;

    @Autowired
    private AuthFeignClient authFeignClient;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    public Aluno findByEmail(String email) {
        return rep.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email));
    }

    public Aluno findByCpf(String cpf) {
        return rep.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException(cpf));
    }

    public List<Aluno> findAll() {
        return rep.findAll();
    }

    public Aluno findById(Long id) {
        return rep.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Aluno insert(Aluno aluno, String senha) {
        Aluno salvo = rep.save(aluno);

        CriarUsuarioDTO usuarioDTO = new CriarUsuarioDTO(
                salvo.getNome(),
                salvo.getEmail(),
                senha,
                "ROLE_ALUNO"
        );

        authFeignClient.criarUsuario(clientId, clientSecret, usuarioDTO);

        return salvo;
    }

    @Transactional
    public Aluno update(Long id, Aluno obj) {
        try {
            Aluno entity = rep.getReferenceById(id);
            entity.setNome(obj.getNome());
            entity.setEmail(obj.getEmail());
            entity.setCpf(obj.getCpf());
            entity.setDataNascimento(obj.getDataNascimento());
            return rep.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            rep.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}