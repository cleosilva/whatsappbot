package com.whatsappstonksbot.bot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagingController {
    private final Set<String> repeatCallers = Collections.synchronizedSet(new HashSet<>());

    @PostMapping(path="/messages", produces = "text/plain")
    public String service(@RequestParam("From") String fromNumber,
                          @RequestParam("Body") String symbol) {
        
        if(!repeatCallers.contains(fromNumber)){
            repeatCallers.add(fromNumber);
            return "Welcome to stonksbot \uD83D\uDCC8. Text me a valid ticker symbol and I'll give you quote data.";
        }

        Optional<Price> price = FinnhubService.getStockDetails(symbol);

        if(price.isPresent()){
            return messageBuilder(price.get(), symbol);
        } else {
            return "Sorry. We couldn't find any info on that one. Try another.";
        }

    }

    private String messageBuilder(Price price, String symbol) {
        return String.format("Here are the most up-to-date %s prices:\n" +
                        "\uD83C\uDF05 Open price of the day: $%s\n" +
                        "\uD83D\uDCC8 High price of the day: $%s\n" +
                        "\uD83D\uDCC9 Low price of the day: $%s\n" +
                        "\uD83D\uDD14 Current price: $%s\n" +
                        "\uD83D\uDCC6 Previous close price: $%s\n" +
                        "\uD83E\uDD1E Send another symbol.",
                symbol.toUpperCase(), price.open, price.high, price.low, price.current, price.close);
    }

}
