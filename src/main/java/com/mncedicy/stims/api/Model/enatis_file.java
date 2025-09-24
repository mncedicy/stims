package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class enatis_file {
    @Id
    public long enatis_file_id;
    public String enatis_file_input_name;
    public String enatis_file_output_name;
    public String enatis_file_type;
    public String enatis_file_input_ext;
    public String enatis_file_output_ext;
    @Column(length = 9999)
    public String enatis_file_export_content;
    @Column(length = 2000)
    public String enatis_file_import_content;
    public LocalDateTime enatis_file_export_date;
    public LocalDateTime enatis_file_import_date;
    public int enatis_file_size;
    public int enatis_file_exported_records;
    public int enatis_file_imported_records;
    @Column(columnDefinition = "varchar(255) default 'Exported'", nullable = false)
    public String enatis_file_status = "Exported";
    public long enatis_file_exported_by;
    public String enatis_file_exported_by_name;
    public long enatis_file_imported_by;
    public String enatis_file_imported_by_name;
    public int enatis_file_client_id;

}


