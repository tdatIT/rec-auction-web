package com.ec.recauctionec.location;

import com.ec.recauctionec.entities.AddressData;
import com.ec.recauctionec.entities.Delivery;
import com.ec.recauctionec.repositories.DeliveryRepo;
import com.ec.recauctionec.repositories.UserAddressRepo;
import com.ec.recauctionec.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculateShipCost {
    @Autowired
    private UserAddressRepo userAddressRepo;
    @Autowired
    private ProductService productService;
    @Autowired
    private DeliveryRepo deliveryRepo;

    @RequestMapping(value = "api/v1/shipping-cost", method = RequestMethod.GET)
    public ResponseEntity<ShipCostObject> calculateShipping(@RequestParam("addressId") int desId,
                                                            @RequestParam("location") int srcId) {
        ShipCostObject obj;
        try {
            AddressData src = userAddressRepo.findById(srcId).orElseThrow();
            AddressData des = userAddressRepo.findById(desId).orElseThrow();
            Delivery delivery = deliveryRepo.findById(1).orElseThrow();
            //Set default Viettel Post
            double ship_cost = Shipping.calculateShipping(src, des, delivery);
            obj = new ShipCostObject("Tinh thanh cong "
                    + src.getAddressDetailInfo() + "->" + des.getAddressDetailInfo(),
                    (int) ship_cost);
        } catch (Exception e) {
            obj = new ShipCostObject("FAILS", 0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

}
