package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CLIENT")
public class Client extends OtherParty {
    public Client() {
    }

    public Client(long id, String name) {
        super(id, name);
    }

    public void setId(Long id) {
        this.id = id;
    }
}
