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
            return "Bem-vindo ao stonksbot \uD83D\uDCC8. Envie um código válido e eu lhe mandarei a cotação.";
        }

        Optional<Price> price = FinnhubService.getStockDetails(symbol);

        if(price.isPresent()){
            return messageBuilder(price.get(), symbol);
        } else {
            return "Desculpe, não encontramos nenhuma informação sobre esta ação. Tente novamente.";
        }

    }

    private String messageBuilder(Price price, String symbol) {
        return String.format("Aqui estão os preços %s mais atualizados:\n" +
                        "\uD83C\uDF05 Preço de abertura do dia: $%s\n" +
                        "\uD83D\uDCC8 Preço máximo do dia: $%s\n" +
                        "\uD83D\uDCC9 Preço mínimo do dia: $%s\n" +
                        "\uD83D\uDD14 Preço atual: $%s\n" +
                        "\uD83D\uDCC6 Preço de fechamento anterior: $%s\n" +
                        "\uD83E\uDD1E Enviar outro código.",
                symbol.toUpperCase(), price.open, price.high, price.low, price.current, price.close);
    }

}
