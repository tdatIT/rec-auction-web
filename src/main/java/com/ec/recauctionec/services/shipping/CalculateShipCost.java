package com.ec.recauctionec.services.shipping;

import com.ec.recauctionec.data.entities.AddressData;
import com.ec.recauctionec.data.entities.Delivery;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.repositories.DeliveryRepo;
import com.ec.recauctionec.data.repositories.UserAddressRepo;
import com.ec.recauctionec.services.OrderService;
import com.ec.recauctionec.services.ProductService;
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
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "api/v1/shipping-cost", method = RequestMethod.GET)
    public ResponseEntity<ShipCostObject> calculateShipping(@RequestParam int addressId,
                                                            @RequestParam int orderId) {
        ShipCostObject obj = null;
        try {
            Orders order = orderService.findById(orderId);
            AddressData des = userAddressRepo.findById(addressId).orElseThrow();
            if (order != null && des != null) {
                //Get address of supplier
                AddressData src = order.getProduct()
                        .getSupplier()
                        .getAddresses().get(0);
                Delivery delivery = deliveryRepo.findById(1).orElseThrow();
                //Set default Viettel Post
                double ship_cost = Shipping.calculateShipping(src, des, delivery);
                obj = new ShipCostObject("Tinh thanh cong "
                        + src.getAddressDetailInfo() + "->" + des.getAddressDetailInfo(),
                        (int) ship_cost);
            }
        } catch (Exception e) {
            obj = new ShipCostObject("FAILS", 0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

}
