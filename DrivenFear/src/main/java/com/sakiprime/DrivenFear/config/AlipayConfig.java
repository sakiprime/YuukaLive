package com.sakiprime.DrivenFear.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @PostConstruct
    public void initAlipay() {
        Factory.setOptions(getOptions());}

    private Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";

        config.appId = "9021000162670842";
        config.merchantPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyXNoTT1sHE6SFGKZVOs7Xl/IwjkdD67gkJ0475iNgPkFdVALhpsVpnKsI5CojEknHYQSN//JfxkVz0bJixESCv77BwTjw6fg04F1EJf6vB1+zCKbf+/+yJCbvHcbkaooThnyn+idGUaC3wSY1LeUM4xwwN9YZY+t0m6aWi5axb3Z+mw5TFUmyFeHH6cb2zbUlBpuC9DLLoSCz02+r6buPramjvqeGFZJy9MjqXYXAOIHqqY2PaHXJR7ItUkXY63zjVQRed4+rFONmwsqUlrI9KqKnMjx+IjkfRoVoXROCnrgvUFt4XWMJxFUrhzQ+9f1wGipDA7E7tsZpHU2CJ3MjAgMBAAECggEAA/RVDWqL/RstYZoLTgZwwYzn3LW61MGsInkQnflbPW4D6vrK2Y/l/pJvpFHSsVZddmMrQ66yA/eUYJ38k34AetmfdBJBne3CCAIVb0ZcYCsMrx54/KxMnfl5N4H0f+nwx8AKkXrVVhTHOGu62iD7XA7I+DS0tXlmpf4xclMD80kY3JTpHHeEaJ1RbSLagJiVOlvbHS524I4OKP7xTer1hce5wtNnzXQUaWl+q7ojKYE5TYvQeY1OS1n60/cXteFj4auRYVGKNXlAFwBKQJjriZc5knQcx+Il6WoJhxlcIktN1yOxoJn5olgbixpJY/7myXddSVAAdA7H51h7o+OZsQKBgQDiq7UHXfgpgG4WrU70TENlIFuUiYA35gvy07bT0GNpjfLnrtBdDMIsejxB6zo6zH5Cy1TPoTq2DWK/83T32WFzW39tVVQjCQezgTTeJoiq+PXmPLXVcTOAPBWMlrstFZtJkkH+5bd75kKaOZjoAq/VYUPb1KxKrpTg1rXBm05ImwKBgQDJcPqE3WTbGvm2lhANFMQiCywW0x5XvnsnWPVEJBFBI4OEBJUHjDLT02K1pasnEqsg29SmbtWBmaproPKuM38n0vyx1g7kylFFsxkRGL/0JfGUemX8SCTZB9jjeek3ARwTGB3oPL0eOqH+CQqTt7rUqBfkPz+oFRuPkfvmC0bUGQKBgA2V7Qu7/hu+rtp9GLZbu85b2iFU/HJdP2oWdmbLnqm88EoimCp7kUfJK8Nnrd3IU+j89uBa8YHn23tVxDT2uniHi2OqMZjH+cYwgFKwTJCyy0o5aUyZtFSDRWdfwWg6W0xVj88PRagPbP6BZUDCqHdJlR+f55OyUwoG1G3+OPrxAoGALPl0dUWYxvJ23jUS0FjakV89MLtiAuUcSeSqndQPpiNvsYH0ZAMBNhnz5+pdFBQu8N8j0yTbtlvAmNcOV6ZXtWR91pacLKifDJi3+AE2miP+k8/gBTt2Pp5p5h/J2eX9hqXTx44ICz+7+yO5oNFcfS4qZ2NXqwdLZ/qH6wyJe8ECgYAem2tka/aQbIMSzPOiRZKQh7ng0cP5SemmdQqRl1tmfDlIcz2L0kNMWvZBrmBCP8ZDFrX7mRYxLP8kEIjWrm6sluzVJdXO18pBv11+GGH7DrxHcf6HZJxQiNOGxMxoi8lvoz88ScCBKx+/unppgfTouhtyPw5nVEbr8lMiw7i1oQ==";
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwij9MXpG5k25oZwVAv44lZQF4akzU3zeWG4xFydvmkrJMdwQ1Q/EI2j5XFXEIOMtneSOpS5NGr7kDXDQk62NGW92rWfrQpACot+2YGAS0i05F1B2qH0BOujl3W1rdgUExvgogjzK0T47IwMdMQsjxOrJpn+3zKlzz0SoGb4f3G0P1cfYCk8gJEB0LUUYz9u9xJr7b6POSisY5BC0msFHQn7L3PQnYSX8c1+N18yzQqymrNK06Y5+i2l0AexIdOZWDXkb5Pp77jtUMI/yZxuziNFerJwDrRbHXdrlE7GAhDvbTniWY1sMHeq5yI0JvQH1KdyijQ0M8yahF2oDv0RKzQIDAQAB";

        // 异步通知地址
        config.notifyUrl = "http://localhost:5173/user/payloading";

        return config;
    }
}
