package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "SUPPLIER")
public class Supplier extends OtherParty {
    public Supplier() {
    }

    public Supplier(long id, String name) {
        super(id, name);
    }

    public void setId(Long id) {
        this.id = id;
    }
}
