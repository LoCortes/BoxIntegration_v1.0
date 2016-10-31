package net.locortes.box.java.sdk;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicenc.cortesolea on 28/10/2016.
 */
public class Folder {
    private String id;
    private String name;
    private int deepness; //How many levels there are hanging from this one
    private List<Folder> folderChildren;
    private List<Document> documentChildren;
    private List<Collaborator> collaborators;

    public Folder(String id, String name) {
        this.id = id;
        this.name = name;
        this.folderChildren = new ArrayList<>();
        this.documentChildren = new ArrayList<>();
        this.collaborators = new ArrayList<>();
        this.deepness = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getDeepness() {
        return deepness;
    }

    public void setDeepness(int deepness) {
        this.deepness = deepness;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFolder(Folder folder) {
        this.folderChildren.add(folder);
    }

    public List<Folder> getFolderChildren() {
        return folderChildren;
    }

    public void addDocument(Document document) {
        this.documentChildren.add(document);
    }

    public List<Document> getDocumentChildren() {
        return documentChildren;
    }

    public void addCollaborator(Collaborator collaborators) {
        this.collaborators.add(collaborators);
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    @Override
    public String toString() {
        return toString(0, true, true);
    }

    /**
     *
     * @param printCollaborators
     * @param printDocuments
     * @return
     */
    public String toString(boolean printCollaborators, boolean printDocuments) {
        return toString(0, printCollaborators, printDocuments);
    }

    /**
     *
     * @param level
     * @param printCollaborators
     * @param printDocuments
     * @return
     */
    private String toString(int level, boolean printCollaborators, boolean printDocuments) {
        StringBuilder builder = new StringBuilder(StringUtils.repeat("\t", level));

        //Indicate if the folder is a leaf folder or not
        builder.append(this.getFolderChildren().size() > 0 ? "+" : "-");

        //Name and ID
        builder.append(" Folder Name: ").append(this.name);
        builder.append(" - Id: ").append(this.id);
        builder.append("\n");

        //Collaborators
        if(printCollaborators)
            for(Collaborator collaborator: this.getCollaborators())
                builder.append(collaborator.toString(level+1));

        //Documents
        if(printDocuments)
            for(Document document : this.getDocumentChildren())
                builder.append(document.toString(level+1));

        //Folders
        for(Folder folder: this.getFolderChildren())
            builder.append(folder.toString(level+1, printCollaborators, printDocuments));

        return builder.toString();
    }

    /**
     *
     * @param documentPath
     * @throws IOException
     */
    public void toCSV(String documentPath) throws IOException {
        String csv = this.treeToCSV(0, "");
        File file = new File(documentPath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(csv);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     *
     * @param level
     * @param path
     */
    private String treeToCSV(int level, String path){
        String separator = ";";

        StringBuilder builder = new StringBuilder(path);
        builder.append(this.name).append(separator);

        //Path updated with the new element
        path = builder.toString();

        //Fullfill with empty spaces until its full deepness
        for (int i=0; i<this.deepness; i++)
            builder.append(separator);
        builder.append(this.id).append(separator);
        builder.append("\n");

        //Recursive print of the children
        for(Folder folder: this.getFolderChildren())
            builder.append(folder.treeToCSV(level+1, path));

        return builder.toString();
    }
}
