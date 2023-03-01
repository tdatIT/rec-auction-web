package com.ec.recauctionec.entities;

import com.ec.recauctionec.response.ProvinceResponseAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "address_data")
public class AddressData {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "address_id", nullable = false)
    private int addressId;

    @Column(name = "province", nullable = false)
    private int province;

    @Column(name = "district", nullable = false)
    private int district;

    @Column(name = "ward", nullable = false)
    private int ward;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true)
    private Supplier supplier;

    @OneToMany(mappedBy = "address")
    private Collection<Orders> orders;

    public String getAddressDetailInfo() {
        final String URL = "https://provinces.open-api.vn/api";
        RestTemplate _api = new RestTemplate();
        ProvinceResponseAPI provice = _api
                .getForObject(URL + "/p/" + this.province, ProvinceResponseAPI.class);
        ProvinceResponseAPI district = _api
                .getForObject(URL + "/d/" + this.district, ProvinceResponseAPI.class);
        ProvinceResponseAPI ward = _api
                .getForObject(URL + "/w/" + this.ward, ProvinceResponseAPI.class);
        return addressDetail + " - " + ward.getName() + " - " + district.getName() + " - " + provice.getName();
    }
}
