package Entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlSeeAlso({Offer.class})
public class Offers extends ArrayList<Offer> {
    public Offers() {
    }

    @XmlElement(name = "offer")
    public List<Offer> getOffers() {
        return this;
    }
}
