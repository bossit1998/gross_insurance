package com.example.rest;

import com.example.models.*;
import com.example.services.BondsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BondsController {

    @Autowired
    BondsService bondsService;

    // make buy/sell request - request for a buy/sell form
    @PostMapping("/buy-sell-request")
    public ResponseEntity<ResponseData> buySellRequest(@RequestBody BuySellRequestModel buySellRequestModel) {
        return bondsService.buySellRequest(buySellRequestModel);
    }

    // make transfer - first request to get all information about the bond being sold
    @PostMapping("/make-transfer-first")
    public ResponseEntity<ResponseData> makeTransferFirst(@RequestBody FirstTransferModel firstTransferModel) {
        return bondsService.makeTransferFirst(firstTransferModel);
    }

    // make transfer - second request to get full info of the buyer
    @PostMapping("/make-transfer-second")
    public ResponseEntity<ResponseData> makeTransferSecond(@RequestBody UserRequestModel userRequestModel) {
        return bondsService.makeTransferSecond(userRequestModel);
    }

    // approve the transfer by seller
    @PostMapping("/transfer-approved")
    public ResponseEntity<ResponseData> transferApproved(@RequestBody TransferApprovedModel transferApprovedModel) {
        return bondsService.transferApproved(transferApprovedModel);
    }

}
