import javax.xml.soap.*;
import java.util.Scanner;

public class WeatherClient {
    private static final String SOAP_ENDPOINT = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
    private static final String NAMESPACE = "http://WebXml.com.cn/";

    public static void main(String[] args) {
        try {
            // 创建SOAP连接和消息
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapRequest = createSOAPRequest();
            SOAPMessage soapResponse = soapConnection.call(soapRequest, SOAP_ENDPOINT);
            String weather = parseSOAPResponse(soapResponse);
            System.out.println("天气信息:");
            System.out.println(weather);

            // 关闭SOAP连接
            soapConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("web", NAMESPACE);
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapElement = soapBody.addChildElement("getWeatherbyCityName", "web");
        SOAPElement soapArg = soapElement.addChildElement("theCityName", "web");
        Scanner scanner=new Scanner(System.in);
        // 设置要查询的城市名称
        System.out.println("请输入你要查询的城市:");
        String city=scanner.nextLine();
        soapArg.addTextNode(city);
        soapMessage.saveChanges();
        return soapMessage;
    }

    private static String parseSOAPResponse(SOAPMessage soapResponse) throws Exception {
        // 解析SOAP响应并提取天气信息
        SOAPPart soapPart = soapResponse.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody soapBody = envelope.getBody();


        if (soapBody.hasFault()) {
            throw new Exception("SOAP Fault: " + soapBody.getFault().getFaultString());
        }

        // 提取天气信息
        SOAPElement soapResult = (SOAPElement) soapBody.getElementsByTagName("getWeatherbyCityNameResult").item(0);
        String weather = soapResult.getTextContent();

        return weather;
    }
}
