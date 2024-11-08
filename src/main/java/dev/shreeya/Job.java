package dev.shreeya;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String jobDescription;

    public Job() {
    }

    public Job(String companyName, String jobDescription) {
        this.companyName = companyName;
        this.jobDescription = jobDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id) && Objects.equals(companyName, job.companyName) && Objects.equals(jobDescription, job.jobDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, jobDescription);
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                '}';
    }
}
