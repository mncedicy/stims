package com.mncedicy.stims.api.Controller;

import com.mncedicy.stims.api.Classes.*;
import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
//@CrossOrigin(origins = "http://localhost:59536")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("capturing")
public class CapturingController {

    @Autowired
    private BookBatchRepo bookBatchRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookItemRepo bookItemRepo;
    @Autowired
    private InfringerRepo infringerRepo;
    @Autowired
    private NoticeRepo noticeRepo;
    @Autowired
    private ChargeCodeRepo chargeCodeRepo;
    @Autowired
    private CourtClientRepo courtClientRepo;
    @Autowired
    private CourtRepo courtRepo;
    @Autowired
    private VehicleColourRepo vehicleColourRepo;
    @Autowired
    private VehicleMakeRepo vehicleMakeRepo;
    @Autowired
    private VehicleModelRepo vehicleModelRepo;
    @Autowired
    private VehicleTypeRepo vehicleTypeRepo;
    @Autowired
    private VehicleUsageRepo vehicleUsageRepo;
    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private NotificationItemRepo notificationItemRepo;
    @Autowired
    private EnatisFileRepo enatisFileRepo;
    @Autowired
    private enatis_colourRepo enatis_colourRepo;
    @Autowired
    private enatis_makeRepo enatis_makeRepo;
    @Autowired
    private enatis_modelRepo enatis_modelRepo;
    @Autowired
    private enatis_typeRepo enatis_typeRepo;
    @Autowired
    private enatis_usageRepo enatis_usageRepo;
    @Autowired
    ConfigurationController configurationController;

    EnatisData enatisData;
    @GetMapping(value = "/welcome")
    public String getPage(){
        return "Welcome";
    }

    @GetMapping(value = "/getBookBatches")
    @ResponseBody
    public List<book_batch> getBookBatches(@RequestParam int client_id){
        List<book_batch> book_batches= bookBatchRepo.findByClientId(client_id);
        return book_batches;
    }



    @GetMapping(value = "/getBooks")
    @ResponseBody
    public List<book> getBooks(@RequestParam int batch_id){
        return bookRepo.findByBookBatchId(batch_id);
    }

    @GetMapping(value = "/getBookItems")
    @ResponseBody
    public List<book_item> getBookItems(@RequestParam int book_id){
        return bookItemRepo.findByBookId(book_id);
    }

    @GetMapping(value = "/getOfficerBooks")
    @ResponseBody
    public List<book> getOfficerBooks(@RequestParam long book_issued_to){
        return bookRepo.findByBookOfficerId(book_issued_to);
    }




    @GetMapping(value = "/getBookByNotice")
    @ResponseBody
    public Response getBookByNotice(@RequestParam String notice_number_complete){
        Response response = new Response();
        response.setStatus("Error");
        try {
            List<book_item> book_items= bookItemRepo.findByNoticeComplete(notice_number_complete);
            if(!book_items.isEmpty()){
                if(book_items.get(0).book_item_status.equals("New")) {
                        response.setMessage("Notice book not issued");
                }
                else{
                    if(book_items.get(0).book_item_status.equals("Issued")) {
                        response.setMessage("Notice issued to <b>" + book_items.get(0).book_item_book_issued_to_name + "</b>");
                        response.setStatus("Success");
                    }
                    else{
                        response.setMessage("Notice already captured by <b>"+book_items.get(0).book_item_captured_by_name + "</b>");
                    }
                    response.setData(book_items.get(0));
                }
            }
            else{
                response.setMessage("Notice not found");
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }



    @PostMapping(value = "/saveBatch")
    public Response saveBatch(@RequestBody book_batch book_batch){

        Response response = new Response();
        response.setStatus("Error");
        try {
            book_batch.book_batch_last_number=book_batch.book_batch_first_number+(book_batch.book_batch_books_count*book_batch.book_batch_pages_per_book) -1;
            book_batch.book_batch_last_number_complete = book_batch.book_batch_type_code+"/"+book_batch.book_batch_last_number+"/"+book_batch.book_batch_authority_code;
            book_batch.book_batch_status = "new";
            book_batch.book_batch_timestamp = LocalDateTime.now();
            book_batch.book_batch_capture_date = LocalDateTime.now();
            book_batch = bookBatchRepo.save(book_batch);

            List<book> books = new ArrayList<>();
            for (int i=0;i<book_batch.book_batch_books_count;i++){
                book book = new book();
                book.book_authority_code =book_batch.book_batch_authority_code;
                book.book_pages = book_batch.book_batch_pages_per_book;
                book.book_status = "new";
                book.book_type_code = book_batch.book_batch_type_code;
                book.book_type_name = book_batch.book_batch_type_name;
                book.book_book_batch_id = book_batch.book_batch_id;
                book.book_authority_name = book_batch.book_batch_authority_name;
                book.book_client_id = book_batch.book_batch_client_id;
                book.book_first_number = book_batch.book_batch_first_number+(i* book.book_pages);
                book.book_first_number_complete =   book.book_type_code+"/"+book.book_first_number+"/"+book.book_authority_code;
                book.book_last_number = book.book_first_number+book.book_pages-1;
                book.book_last_number_complete =   book.book_type_code+"/"+book.book_last_number+"/"+book.book_authority_code;
                book.book_pages_left = book.book_pages;
                book.book_timestamp = LocalDateTime.now();
                books.add(book);
            }
            books = bookRepo.saveAll(books);
            List<book_item> book_items = new ArrayList<>();
            List<String> noticesList = new ArrayList<>();
            for (book value : books) {
                StringBuilder noticeList = new StringBuilder();
                for (int i = 0; i < book_batch.book_batch_pages_per_book; i++) {

                    book_item book_item = new book_item();
                    book_item.book_item_book_batch_id = value.book_book_batch_id;
                    book_item.book_item_book_id = value.book_id;
                    book_item.book_item_notice_number = value.book_first_number + i;
                    book_item.book_item_notice_number_complete = value.book_type_code + "/" + book_item.book_item_notice_number + "/" + value.book_authority_code;
                    book_item.book_item_client_id = value.book_client_id;
                    book_item.book_item_notice_type = value.book_type_code;
                    book_item.book_item_status = "New";
                    book_item.book_item_timestamp = LocalDateTime.now();
                    book_items.add(book_item);
                    noticeList.append(book_item.book_item_notice_number_complete);
                    if (i < book_batch.book_batch_pages_per_book - 1)
                        noticeList.append("#");
                }
                value.book_notice_list = noticeList.toString();
            }
            books = bookRepo.saveAll(books);
            book_items = bookItemRepo.saveAll(book_items);
            BookBatchData bookBatchData = new BookBatchData();
            bookBatchData.book_batch = book_batch;
            bookBatchData.books = books;
            bookBatchData.book_items = book_items;
            response.setData(bookBatchData);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/issueBook/{id}")
    public Response issueBook(@PathVariable long id,@RequestBody book book){

        Response response = new Response();
        response.setStatus("Error");
        try {
            book updateBook = bookRepo.findById(id).get();
            updateBook.book_issued_by_name = book.book_issued_by_name;
            updateBook.book_issued_by = book.book_issued_by;
            updateBook.book_issued_to = book.book_issued_to;
            updateBook.book_issued_to_name = book.book_issued_to_name;
            updateBook.book_issue_date = book.book_issue_date;
            updateBook.book_status = book.book_status;
            updateBook.book_last_update = LocalDateTime.now();
            updateBook = bookRepo.save(updateBook);

            List<book_item> items = bookItemRepo.findByBookId(updateBook.book_id);
            for(book_item item : items){
                item.book_item_book_issued_by_name = book.book_issued_by_name;
                item.book_item_book_issued_by = book.book_issued_by;
                item.book_item_book_issued_to = book.book_issued_to;
                item.book_item_book_issued_to_name = book.book_issued_to_name;
                item.book_item_book_issue_date = book.book_issue_date;
                item.book_item_status = "Issued";
            }

            bookItemRepo.saveAll(items);


            response.setData(updateBook);
            response.setMessage("Successfully Issued");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/HandinBook/{id}")
    public Response HandinBook(@PathVariable long id,@RequestBody book book){

        Response response = new Response();
        response.setStatus("Error");
        try {
            book updateBook = bookRepo.findById(id).get();
            updateBook.book_handedin_by_name = book.book_handedin_by_name;
            updateBook.book_handedin_by = book.book_handedin_by;
            updateBook.book_handedin_date = book.book_handedin_date;
            updateBook.book_status = book.book_status;
            updateBook.book_last_update = LocalDateTime.now();
            updateBook = bookRepo.save(updateBook);
            response.setData(updateBook);
            response.setMessage("Successfully Issued");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @GetMapping(value = "/getNoticeByBookId")
    @ResponseBody
    public List<InfringementData> getNoticeByBookId(@RequestParam long infringement_notice_book_id){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices= noticeRepo.findByBookId(infringement_notice_book_id);
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringements.add(infringement);
        }
        return infringements;
    }


    @GetMapping(value = "/getNoticeByCapturedBy")
    @ResponseBody
    public List<InfringementData> getNoticeByCapturedBy(@RequestParam long captured_by){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices= noticeRepo.findByCapturedById(captured_by);
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringements.add(infringement);
        }
        return infringements;
    }


    @GetMapping(value = "/getNoticeByReference")
    @ResponseBody
    public List<InfringementData> getNoticeByReference(@RequestParam int client_id,@RequestParam String reference){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices= noticeRepo.findByClientReference(client_id,reference);
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringements.add(infringement);
        }
        return infringements;
    }



    @GetMapping(value = "/getEnatis")
    @ResponseBody
    public List<InfringementData> getEnatis(@RequestParam int client_id){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices = noticeRepo.findByClientEnatisActive(client_id);
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            infringement.charge_code = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code).get(0);
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            infringements.add(infringement);
        }
        return infringements;
    }

    @GetMapping("/exportEnatis")
    public ResponseEntity<Resource> exportEnatis(@RequestParam int client_id,@RequestParam long exported_by,
                                                 @RequestParam String exported_by_name) throws IOException {
        List<infringement_notice> notices =  noticeRepo.findByClientEnatisActiveExport(client_id);
        String fileName = (client_id<10?(client_id+"0"):(client_id+"")) +"00001";
        List<enatis_file> files= enatisFileRepo.findLastByClientId(client_id);
        if(!files.isEmpty())
            fileName= (files.get(0).enatis_file_id+1)+"";

        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileContent = "";
        for(infringement_notice notice:notices){
            String reg = notice.infringement_notice_registration;
            String len = (reg.length() > 9) ? reg.length()+"" : ("0" + reg.length());
            String datee = notice.infringement_notice_offence_date.format(formatterMonth);
            String tick = notice.infringement_notice_reference;
            int lentick = tick.length();
            String str = "5040" + fileName + "003" + len + reg + "06008" + datee + "065" + lentick + tick + "99901";
            fileContent += str + "\n";

            notice.infringement_notice_enatis_export_count++;
            notice.infringement_notice_enatis_status = "Exported";
            notice.infringement_notice_enatis_file = Long.parseLong(fileName);
        }
        fileContent += "5990" + fileName + "90007" + fileName + "99901";

        noticeRepo.saveAll(notices);

        byte[] contentBytes = fileContent.getBytes();
        ByteArrayResource resource = new ByteArrayResource(contentBytes);


        enatis_file file = new enatis_file();
        file.enatis_file_id = Long.parseLong(fileName);
        file.enatis_file_exported_by = exported_by;
        file.enatis_file_exported_by_name = exported_by_name;
        file.enatis_file_export_content = fileContent;
        file.enatis_file_client_id = client_id;
        file.enatis_file_export_date = LocalDateTime.now();
        file.enatis_file_exported_records = notices.size();
        file.enatis_file_input_ext =".inp";
        file.enatis_file_input_name = fileName+".inp";
        file.enatis_file_output_ext =".out";
        file.enatis_file_output_name = fileName+".out";
        file.enatis_file_size = contentBytes.length;
        file.enatis_file_status = "Exported";
        enatisFileRepo.save(file);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName+".inp");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", fileName+".inp");
        header.add("Expires", "0");
        header.add("fileName", fileName+".inp");

        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(contentBytes.length).eTag(fileName+".inp")
                .body(resource);

    }


    public EnatisData getEnatisData() {
        EnatisData enatisData = new EnatisData();
        enatisData.makes = enatis_makeRepo.findAll();
        enatisData.models = enatis_modelRepo.findAll();
        enatisData.colours = enatis_colourRepo.findAll();
        enatisData.types = enatis_typeRepo.findAll();
        enatisData.usages = enatis_usageRepo.findAll();
        return  enatisData;
    }

    @PostMapping(value = "/importEnatis")
    @ResponseBody
    public Response importEnatis(@RequestBody List<String> lines,@RequestParam int client_id,
                                                 @RequestParam long imported_by,@RequestParam String imported_by_name) throws IOException {
        enatisData = getEnatisData();
        Response response = new Response();
        response.setStatus("Error");
        int pos = 0,posee=0;
       try {
            List<infringement_notice> notices = new ArrayList<>();
            List<infringement_infringer> infringers = new ArrayList<>();
            for (String line:lines){
                String found = line.substring(1,4);
                String reference="";
                posee=0;
                if(found.equals("050")) {
                    reference = getValueBetween(line, "065", "066");
                    List<infringement_notice> noticess = noticeRepo.findByClientReferenceEnatisActive(client_id, reference).stream().toList();
                    if (!noticess.isEmpty()) {
                        posee++;
                        infringement_notice notice = noticess.get(0);
                        infringement_infringer infringer = infringerRepo.findByNoticeId(notice.infringement_notice_id).get(0);
                        notice.infringement_notice_enatis_status = "Imported";
                        notice.infringement_notice_enatis = "Done";
                        if (getValueBetween(line, "014", "015").equals("02")) {
                            infringer.infringement_infringer_id_number = getValueBetween(line, "015", "016");
                            infringer.infringement_infringer_middle_name = getValueBetween(line, "017", "018");
                            infringer.infringement_infringer_surname = getValueBetween(line, "016", "017");
                            infringer.infringement_infringer_name = infringer.infringement_infringer_middle_name;
                            infringer.infringement_infringer_ownership_type = "Individual";
                        } else {
                            posee++;
                            infringer.infringement_infringer_id_number = getValueBetween(line, "053", "061");
                            posee++;
                            infringer.infringement_infringer_company_id = getValueBetween(line, "015", "016");
                            posee++;
                            infringer.infringement_infringer_company = getValueBetween(line, "016", "017", "018");
                            posee++;
                            infringer.infringement_infringer_middle_name = getValueBetween(line, "033", "034");
                            posee++;
                            infringer.infringement_infringer_surname = getValueBetween(line, "032", "033");
                            posee++;
                            infringer.infringement_infringer_name = infringer.infringement_infringer_middle_name;
                            posee++;
                            infringer.infringement_infringer_ownership_type = "Business";
                        }

                        infringer.infringement_infringer_cellphone = getValueBetween(line, "021", "023");
                        notice.infringement_notice_cellphone = infringer.infringement_infringer_cellphone;

                        notice.infringement_notice_id_number = infringer.infringement_infringer_id_number;
                        notice.infringement_notice_name = infringer.infringement_infringer_name + " " + infringer.infringement_infringer_surname;

                        infringer.infringement_infringer_postal_address = getValueBetween(line, "019", "020", "021", "022");
                        infringer.infringement_infringer_postal_suburb = (getValueBetween(line, "020", "021", "022", "023").length() > 0) ? getValueBetween(line, "020", "021", "022", "023") : (getValueBetween(line, "022", "023", "024", "025").length() > 0) ? getValueBetween(line, "022", "023", "024", "025") : getValueBetween(line, "023", "024", "025", "026");
                        infringer.infringement_infringer_postal_city = (getValueBetween(line, "023", "024", "025", "026").length() > 0) ? getValueBetween(line, "023", "024", "025", "026") : (getValueBetween(line, "022", "023", "024", "025").length() > 0) ? getValueBetween(line, "022", "023", "024", "025") : getValueBetween(line, "020", "021", "022", "023");
                        infringer.infringement_infringer_postal_code = getValueBetween(line, "024", "025", "026", "027");

                        boolean isIntString = tryParse(getValueBetween(line, "026", "027", "028", "029")) && !tryParse(getValueBetween(line, "025", "026", "027", "028"));
                        infringer.infringement_infringer_physical_address = (isIntString) ? getValueBetween(line, "026", "027", "028", "029") : getValueBetween(line, "025", "026", "027", "028");
                        infringer.infringement_infringer_physical_suburb = (isIntString) ? getValueBetween(line, "025", "026", "027", "028") : getValueBetween(line, "026", "027", "028", "029");
                        infringer.infringement_infringer_physical_city = (getValueBetween(line, "027", "028", "028", "030").length() > 0) ? getValueBetween(line, "027", "028", "029", "030") : infringer.infringement_infringer_physical_suburb;
                        infringer.infringement_infringer_physical_code = getValueBetween(line, "030", "031", "032", "034");

                        notice.infringement_notice_vehicle_make = makeById(getValueBetween(line, "034", "035"));
                        notice.infringement_notice_vehicle_model = modelById(getValueBetween(line, "035", "036", "037"));
                        notice.infringement_notice_vehicle_usage = usageById(getValueBetween(line, "041", "042"));
                        notice.infringement_notice_vehicle_type = typeById(getValueBetween(line, "042", "043"));
                        notice.infringement_notice_vehicle_colour = colourById(getValueBetween(line, "043", "053", "061"));

                        notice.infringement_notice_vehicle_license_expire = createDate(getValueBetween(line, "153", "999"));
                        notice.infringement_notice_vehicle_ownership_date = createDate(getValueBetween(line, "009", "010"));
                        notice.infringement_notice_vehicle_first_reg_date = createDate(getValueBetween(line, "037", "040"));

                        notices.add(notice);
                        infringers.add(infringer);
                        notice.infringement_notice_saved_note = "Enatis Imported";
                        notice.infringement_notice_enatis_verified_by = imported_by;
                        notice.infringement_notice_enatis_verified_by_name = imported_by_name;
                        sendMessage(notice);
                        saveNoticeHistory(notice);
                    }
                }
                else if(found.equals("010")){
                    reference = getValueBetween(line,"065","999");
                    List<infringement_notice> noticess = noticeRepo.findByClientReferenceEnatisActive(client_id, reference).stream().toList();
                    if (!noticess.isEmpty()) {
                        posee++;
                        infringement_notice notice = noticess.get(0);
                        infringement_infringer infringer = infringerRepo.findByNoticeId(notice.infringement_notice_id).get(0);
                        notice.infringement_notice_enatis_status = "Not Found";
                        if(notice.infringement_notice_enatis_export_count>3)
                            notice.infringement_notice_enatis = "Done";

                        notices.add(notice);
                        infringers.add(infringer);

                        notice.infringement_notice_saved_note = "Enatis Imported";
                        notice.infringement_notice_enatis_verified_by = imported_by;
                        notice.infringement_notice_enatis_verified_by_name = imported_by_name;
                        sendMessage(notice);
                        saveNoticeHistory(notice);
                    }
                }

                pos++;
            }

           noticeRepo.saveAll(notices);
           infringerRepo.saveAll(infringers);




            response.setData(notices);
           response.setData1(infringers);
            response.setData1(infringers);
            response.setMessage("Imported successful: "+notices.size()+" updated");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }



    public String makeById(String id)
    {
        String res = "";
        for (enatis_make item : enatisData.makes)
        {
            if (item.enatis_make_code.equals(id) && item.enatis_make_language_id==1)
                res = item.enatis_make_description;
        }
        res = (res.length() == 0) ? "Unknown" : res;
        return res;
    }

    public String modelById(String id)
    {
        String res = "";
        if(!id.isEmpty()) {
            id = (id.charAt(0) == '0') ? id.substring(1) : id;
            for (enatis_model item : enatisData.models) {
                if (item.enatis_model_code == id && item.enatis_model_language_id == 1)
                    res = item.enatis_model_description;
            }
            res = (res.length() == 0) ? "Unknown" : res;
        }
        return res;
    }

    public String usageById(String id)
    {
        String res = "";
        if(!id.isEmpty()) {
            id = (id.charAt(0) == '0') ? id.substring(1) : id;
            for (enatis_usage item : enatisData.usages) {
                if (item.enatis_usage_code == id && item.enatis_usage_language_id == 1)
                    res = item.enatis_usage_description;
            }
            res = (res.length() == 0) ? "Unknown" : res;
        }
        return res;
    }

    public String colourById(String id)
    {
        String res = "";
        if(!id.isEmpty()) {
            id = (id.charAt(0) == '0') ? id.substring(1) : id;
            for (enatis_colour item : enatisData.colours) {
                if (item.enatis_colour_code == id && item.enatis_colour_language_id == 1)
                    res = item.enatis_colour_description;
            }
            res = (res.length() == 0) ? "Unknown" : res;
        }
        return res;
    }

    public String typeById(String id)
    {
        String res = "";
        if(!id.isEmpty()) {
            id = (id.charAt(0) == '0') ? id.substring(1) : id;
            for (enatis_type item : enatisData.types) {
                if (item.enatis_type_code == id && item.enatis_type_language_id == 1)
                    res = item.enatis_type_description;
            }
            res = (res.length() == 0) ? "Unknown" : res;
        }
        return res;
    }






    public LocalDate createDate(String str)
    {
        String ndate = "";
        if (str.length() > 7)
            ndate = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6);
        LocalDate dd = null;
        if (ndate.length() > 0)
            dd = LocalDate.parse(ndate);
        return dd;
    }
    public boolean tryParse(String obj) {
        try {
            Integer.parseInt(obj);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    public String getValue(String line,String code){
            int index = line.lastIndexOf(code);
        int length = Integer.parseInt(line.substring(index+3,index+5));
        String value = line.substring(index+5,index+5+length);
        return value;
    }

    public String getValueBetween(String line,String code,String code1){
        String value ="";
        int index = line.indexOf(code);
        while (index >= 0) {
            try {
                String item= line.substring(index);
                if(value.isEmpty()){
                    int length = Integer.parseInt(item.substring(3,5).trim());
                    if(length<50 && length<item.length()+10)
                        if(item.substring(5+length).startsWith(code1))
                            value = item.substring(5,5+length);
                }
            }catch (StringIndexOutOfBoundsException e) {
                value ="";
            }
            catch (NumberFormatException e) {
                value ="";
            }
            catch (Exception e) {
                value ="";
            }
            index = line.indexOf(code, index + 1);
        }

        return value;
    }

    public String getValueBetween(String line,String code,String code1,String code2){
        String value ="";
        int index = line.indexOf(code);
        while (index >= 0) {
            try {
                String item= line.substring(index);
                if(value.isEmpty()){
                    int length = Integer.parseInt(item.substring(3,5).trim());
                    if(length<50 && length<item.length()+6)
                        if(item.substring(5+length).startsWith(code1) || item.substring(5+length).startsWith(code2))
                            value = item.substring(5,5+length);
                }
            }catch (StringIndexOutOfBoundsException e) {
                value ="";
            }
            catch (NumberFormatException e) {
                value ="";
            }
            catch (Exception e) {
                value ="";
            }
            index = line.indexOf(code, index + 1);
        }

        return value;
    }


    public String getValueBetween(String line,String code,String code1,String code2,String code3){
        String value ="";
        int index = line.indexOf(code);
        while (index >= 0) {
            try {
                String item= line.substring(index);
                if(value.isEmpty()){
                    int length = Integer.parseInt(item.substring(3,5).trim());
                    if(length<50 && length<item.length()+6)
                        if(item.substring(5+length).startsWith(code1) || item.substring(5+length).startsWith(code2) || item.substring(5+length).startsWith(code3))
                            value = item.substring(5,5+length);
                }
            }catch (StringIndexOutOfBoundsException e) {
                value ="";
            }
            catch (NumberFormatException e) {
                value ="";
            }
            catch (Exception e) {
                value ="";
            }
            index = line.indexOf(code, index + 1);
        }

        return value;
    }



        @GetMapping(value = "/getDefaultData")
    @ResponseBody
    public DefaultData getDefaultData(@RequestParam int client_id){
        DefaultData defaultData = new DefaultData();
        defaultData.charge_codes = chargeCodeRepo.findByClientId(client_id);
        defaultData.courts = new ArrayList<>();
        List<court_client> court_clients= courtClientRepo.findByClientId(client_id);
        for(court_client court_client : court_clients){
            court court = courtRepo.findById(court_client.court_client_court_id).get();
            defaultData.courts.add(court);
        }
        defaultData.colours = vehicleColourRepo.findAll();
        defaultData.makes = vehicleMakeRepo.findAll();
        defaultData.models = vehicleModelRepo.findAll();
        defaultData.usages = vehicleUsageRepo.findAll();
        defaultData.types = vehicleTypeRepo.findAll();

        return defaultData;
    }



    @PostMapping(value = "/saveNotice")
    public Response saveNotice(@RequestBody InfringementData infringementData){

        Response response = new Response();
        response.setStatus("Error");
        try {

            infringementData.infringer.infringement_infringer_name = Response.capitalize(infringementData.infringer.infringement_infringer_name);
            infringementData.infringer.infringement_infringer_surname = Response.capitalize(infringementData.infringer.infringement_infringer_surname);
            infringementData.infringer = infringerRepo.save(infringementData.infringer);
            infringementData.notice.infringement_notice_name = infringementData.infringer.infringement_infringer_name +" "+infringementData.infringer.infringement_infringer_surname;
            infringementData.notice.infringement_notice_registration = infringementData.notice.infringement_notice_registration.toUpperCase();
            if(infringementData.notice.infringement_notice_offence_date!=null)
            infringementData.notice.infringement_notice_payment_due_date = infringementData.notice.infringement_notice_offence_date.plusDays(32);
            infringementData.notice.infringement_notice_status_date = LocalDateTime.now();
            infringementData.notice.infringement_notice_last_update = LocalDateTime.now();
            infringementData.notice.infringement_notice_id_number = infringementData.infringer.infringement_infringer_id_number;

            infringementData.notice = noticeRepo.save(infringementData.notice);



            if(!infringementData.notice.infringement_notice_saved_note.isEmpty()) {
                 sendMessage(infringementData.notice);
                saveNoticeHistory(infringementData.notice);
            }



            response.setData(infringementData);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }





    @PostMapping(value = "/createNotice")
    public Response createNotice(@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            List<book_item> book_items = bookItemRepo.findByNoticeComplete(notice.infringement_notice_reference);
            if(!book_items.isEmpty()){
                if(book_items.get(0).book_item_status.equals("New")) {
                    response.setMessage("Notice book not issued");
                }
                else{
                    if(book_items.get(0).book_item_status.equals("Issued")) {
                        book_item book_item = book_items.get(0);
                        book_item.setBook_item_status("Captured");
                        book_item.book_item_captured_by = notice.infringement_notice_captured_by;
                        book_item.book_item_captured_by_name = notice.infringement_notice_captured_by_name;
                        book_item.book_item_captured_date = LocalDateTime.now();
                        book_item = bookItemRepo.save(book_item);

                        book book = bookRepo.findById(book_item.book_item_book_id).get();
                        book.increment_pages_completed();
                        bookRepo.save(book);


                        notice.infringement_notice_court_roll_printed = "no";
                        notice.infringement_notice_timestamp = LocalDateTime.now();
                        notice.infringement_notice_status_date = LocalDateTime.now();
                        notice.infringement_notice_last_update = LocalDateTime.now();
                        notice.infringement_notice_month = LocalDateTime.now().getMonthValue();
                        notice.infringement_notice_year = LocalDateTime.now().getYear();
                        notice.infringement_notice_officer_id = book.book_issued_to;
                        notice.infringement_notice_officer_name = book.book_issued_to_name;
                        notice.infringement_notice_book_id = book.book_id;
                        notice.infringement_notice_last_status = "Notice";
                        notice.infringement_notice_status = "Saved";
                        if(notice.infringement_notice_type_name.equals("341 Hand Written")) {
                            notice.infringement_notice_enatis = "Active";
                            notice.infringement_notice_enatis_status = "Ready";
                        }
                        notice.infringement_notice_status_updated_by = notice.infringement_notice_captured_by;
                        notice.infringement_notice_status_updated_by_name = notice.infringement_notice_captured_by_name;
                        notice.infringement_notice_saved_by = notice.infringement_notice_captured_by;
                        notice.infringement_notice_saved_by_name = notice.infringement_notice_captured_by_name;
                        notice.infringement_notice_saved_note = "Notice created by "+notice.infringement_notice_captured_by_name;
                        notice = noticeRepo.save(notice);

                        infringement_infringer infringer = new infringement_infringer();
                        infringer.infringement_infringer_notice_id = notice.infringement_notice_id;
                        infringer.infringement_infringer_client_id = notice.infringement_notice_client_id;
                        infringer.infringement_infringer_notice_reference = notice.infringement_notice_reference;
                        infringer.infringement_infringer_status = "Active";
                        infringer.infringement_infringer_timestamp = LocalDateTime.now();
                        infringer = infringerRepo.save(infringer);

                        InfringementData infringementData = new InfringementData();
                        infringementData.notice = notice;
                        infringementData.infringer = infringer;



                        sendMessage(infringementData.notice);

                        List<notification> notifications= notificationRepo.findBySenderOrReceiverId(infringementData.notice.infringement_notice_captured_by);
                        response.setData1(notifications);

                        response.setData(infringementData);
                        response.setStatus("Success");
                        response.setMessage("Notice created");

                    }
                    else{
                        response.setMessage("Notice already captured by <b>"+book_items.get(0).book_item_captured_by_name + "</b>");
                    }
                }
            }
            else{
                response.setMessage("Notice not found");
            }

        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }




    @PostMapping(value = "/sendMessage")
    public void sendMessage(@RequestBody infringement_notice notice){


        List<notification> notifications= notificationRepo.findByNoticeId(notice.infringement_notice_id);
        notification notification = new notification();
        if(notifications.isEmpty()){
            notification.notification_notice_id = notice.infringement_notice_id;
            notification.notification_client_id = notice.infringement_notice_client_id;
            notification.notification_reference = notice.infringement_notice_reference;
            notification.notification_receiver_name = notice.infringement_notice_officer_name;
            notification.notification_receiver_id = notice.infringement_notice_officer_id;
            notification.notification_sender_id = notice.infringement_notice_saved_by;
            notification.notification_sender_name = notice.infringement_notice_saved_by_name;
            notification.notification_subject = notice.infringement_notice_reference;
            notification.notification_type = "infringement";
        }
        else{
            notification = notifications.get(0);
        }
        notification.notification_status = "Unread";
        notification.notification_last_update = LocalDateTime.now();
        notification.notification_updated_by = notice.infringement_notice_saved_by;
        notification.notification_message = notice.infringement_notice_saved_note;
        notification = notificationRepo.save(notification);


        notification_item item= new notification_item();
        item.notification_item_sent_date = notification.notification_last_update;
        item.notification_item_message = notice.infringement_notice_saved_note;
        item.notification_item_notification_id = notification.notification_id;
        item.notification_item_notice_id = notice.infringement_notice_id;
        item.notification_item_client_id = notice.infringement_notice_client_id;
        item.notification_item_reference = notice.infringement_notice_reference;
        item.notification_item_receiver_name = notice.infringement_notice_officer_name;
        item.notification_item_receiver_id = notice.infringement_notice_officer_id;
        item.notification_item_sender_id = notice.infringement_notice_saved_by;
        item.notification_item_sender_name = notice.infringement_notice_saved_by_name;
        item.notification_item_subject = notice.infringement_notice_reference;
        item.notification_item_type = "infringement";
        item.notification_item_status = "Unread";
        notificationItemRepo.save(item);


    }




    public void saveNoticeHistory(@RequestBody infringement_notice notice){
        history history = new history();
        history.history_reference_id = notice.infringement_notice_id;
        history.history_reference_type = "Infringement";
        history.history_value = notice.infringement_notice_fine_amount;
        history.history_value1 = notice.infringement_notice_charge_short_description;
        history.history_value2 = notice.infringement_notice_charge_description;
        history.history_value3 = notice.infringement_notice_vehicle_make+" "+notice.infringement_notice_vehicle_model+" "+notice.infringement_notice_vehicle_colour;
        history.history_value4 = notice.infringement_notice_offence_location;
        history.history_value5 = notice.infringement_notice_id_number;
        history.history_value6 = notice.infringement_notice_name;
        history.history_value7 = notice.infringement_notice_registration;
        history.history_value8 = notice.infringement_notice_reference;
        history.history_value9 = notice.infringement_notice_saved_note;
        history.history_client_id = notice.infringement_notice_client_id;
        history.history_status = "New";
        history.history_timestamp = LocalDateTime.now();
        history.history_action_by = notice.infringement_notice_captured_by;
        history.history_action_by_name = notice.infringement_notice_captured_by_name;
        history.history_action = notice.infringement_notice_status.equals("Notice")?"Notice Captured":"Notice Saved";
        history.history_action = notice.infringement_notice_enatis != null && notice.infringement_notice_enatis.equals("Done")?"Enatis "+notice.infringement_notice_enatis_status:history.history_action;

        history.history_action_date = LocalDateTime.now();
        history.history_month = LocalDateTime.now().getMonthValue();
        history.history_year = LocalDateTime.now().getYear();
        historyRepo.save(history);
    }

}
