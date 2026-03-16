package com.example.pawcontrolv1.ShelterVet;

/**
 * Model for a pet service location shown on the map and in the list.
 * Types: "vet" | "shelter" | "education"
 */
public class PetService {

    private final String name;
    private final String address;
    private final String contact;
    private final String type;   // "vet", "shelter", "education"
    private final double lat;
    private final double lng;

    public PetService(String name, String address, String contact,
                      String type, double lat, double lng) {
        this.name    = name;
        this.address = address;
        this.contact = contact;
        this.type    = type;
        this.lat     = lat;
        this.lng     = lng;
    }

    public String getName()    { return name;    }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getType()    { return type;    }
    public double getLat()     { return lat;     }
    public double getLng()     { return lng;     }
}