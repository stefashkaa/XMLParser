package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        if(args.length != 2) {
            System.out.println("Args length have to be equal 2!");
            return;
        }
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new FileInputStream("resources/" + args[0]));

        checkAverage(doc);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("resources/" + args[1])));
        t.transform(new DOMSource(doc), new StreamResult(System.out));
    }

    private static void checkAverage(Document doc) {
        NodeList students = doc.getElementsByTagName("student");
        for (int j =0; j < students.getLength(); j++)
        {
            Element e = (Element)students.item(j);
            NodeList subjects = e.getElementsByTagName("subject");

            double sum = 0;
            for (int i = 0; i < subjects.getLength(); i++) {
                Node subject = subjects.item(i);
                String markString = subject.getAttributes().getNamedItem("mark").getNodeValue();
                sum += Integer.parseInt(markString);
            }
            double calculatedAverage = sum / subjects.getLength();

            Node average = e.getElementsByTagName("average").item(0);
            if(average == null) {
                Element element = doc.createElement("average");
                average = e.appendChild(element);
            }
            average.setTextContent(String.valueOf(calculatedAverage));
        }
    }
}
