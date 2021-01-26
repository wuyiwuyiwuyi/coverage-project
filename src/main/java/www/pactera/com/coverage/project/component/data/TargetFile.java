package www.pactera.com.coverage.project.component.data;

import java.io.File;

public class TargetFile {

    private File executionDataFile;
    private File classesDirectory;
    private File sourceDirectory;
    private File mergeDataFile;


    public TargetFile(final File projectDirectory) {

        this.executionDataFile = new File(projectDirectory, "coverage.exec");
        this.classesDirectory = new File(projectDirectory, "\\source\\target\\classes");
        this.sourceDirectory = new File(projectDirectory, "source\\src");

    }

    public TargetFile(final File projectDirectory,String merge){
        this.classesDirectory = new File(projectDirectory, "source\\target\\classes");
        this.sourceDirectory = new File(projectDirectory, "source\\src");
        this.mergeDataFile = new File(projectDirectory, merge);
    }


    public File getExecutionDataFile() {
        return executionDataFile;
    }

    public File getMergeDataFile() {
        return mergeDataFile;
    }

    public void setMergeDataFile(File mergeDataFile) {
        this.mergeDataFile = mergeDataFile;
    }

    public void setExecutionDataFile(File executionDataFile) {
        this.executionDataFile = executionDataFile;
    }

    public File getClassesDirectory() {
        return classesDirectory;
    }

    public void setClassesDirectory(File classesDirectory) {
        this.classesDirectory = classesDirectory;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public TargetFile(File executionDataFile, File classesDirectory, File sourceDirectory) {
        this.executionDataFile = executionDataFile;
        this.classesDirectory = classesDirectory;
        this.sourceDirectory = sourceDirectory;
    }

    public TargetFile() {

    }

    @Override
    public String toString() {
        return "TargetFile{" +
                "executionDataFile=" + executionDataFile +
                ", classesDirectory=" + classesDirectory +
                ", sourceDirectory=" + sourceDirectory +
                '}';
    }
}
