import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class IRDocument {
    private final Document document;
    private final Element docs;
    private int nextDocumentId;

    public IRDocument() throws ParserConfigurationException {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();

        this.document = documentBuilder.newDocument();
        this.docs = document.createElement("docs");
        document.appendChild(docs);
        this.nextDocumentId = 0;
    }

    public void addDocument(String title, String body) {
        var doc = document.createElement("doc");
        docs.appendChild(doc);
        doc.setAttribute("id", String.valueOf(nextDocumentId++));

        var titleElement = document.createElement("title");
        var bodyElement = document.createElement("body");
        titleElement.appendChild(document.createTextNode(title));
        bodyElement.appendChild(document.createTextNode(body));
        doc.appendChild(titleElement);
        doc.appendChild(bodyElement);
    }

    public void toXMLFile(String path) throws TransformerException, FileNotFoundException {
        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        var source = new DOMSource(this.document);
        var result = new StreamResult(new FileOutputStream(path));
        transformer.transform(source,result);
    }
}
