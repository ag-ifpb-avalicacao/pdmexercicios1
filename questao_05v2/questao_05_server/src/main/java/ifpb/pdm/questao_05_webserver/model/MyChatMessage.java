package ifpb.pdm.questao_05_webserver.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Natarajan
 */
@XmlRootElement
@Entity
@SequenceGenerator(name = "message_seq",
        allocationSize = 1,
        initialValue = 1,
        sequenceName = "message_sequencia")
@Table(name = "Message")
public class MyChatMessage implements Serializable {

    @Id
    @GeneratedValue(generator = "message_seq", strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    @Column(name = "is_person_1")
    private boolean isPerson1;

    public MyChatMessage() {
    }

    public MyChatMessage(Long id, String content, boolean isPerson1) {
        this.id = id;
        this.content = content;
        this.isPerson1 = isPerson1;
    }

    public MyChatMessage(String content, boolean isPerson1) {
        this.content = content;
        this.isPerson1 = isPerson1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsPerson1() {
        return isPerson1;
    }

    public void setIsPerson1(boolean isPerson1) {
        this.isPerson1 = isPerson1;
    }
    
}
