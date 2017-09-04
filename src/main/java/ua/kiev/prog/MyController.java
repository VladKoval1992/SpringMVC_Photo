package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class MyController {

    private Map<Long, byte[]> photos = new HashMap<>();
    private Map<Long, byte[]> photosForZip = new HashMap<>();
    private File fileZip = new File("D:\\files.zip");


    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }


    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }


    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/viewallphoto", method = RequestMethod.POST)
    public String viewAllPhoto(Model model) {
        ArrayList<Long> allphoto = new ArrayList<Long>(photos.keySet());
        model.addAttribute("allphoto", allphoto);
        return "work _with_photo";
    }

    @RequestMapping(value = "/deletesomephoto", params = "delete", method = RequestMethod.POST)
    public String deleteSomePhoto(Model model, @RequestParam("check[]") String [] id) {
        for(int i = 0; i < id.length; i++){
            photos.remove(new Long(id[i]));
        }
        ArrayList<Long> allphoto = new ArrayList<Long>(photos.keySet());
        model.addAttribute("allphoto", allphoto);
        return "work _with_photo";
    }

    @RequestMapping(value = "/deletesomephoto", params = "zipfille", method = RequestMethod.POST)
    public String zipFilledowloand (@RequestParam("check[]") String [] id) {
        if (photos.size() < 1) {
            return "index";
        }
        if (id.length < 1) {
            return "index";
        }
        for(int i = 0; i < id.length; i++){
            photosForZip.put(new Long(id[i]), photos.get(new Long(id[i])));
        }
        return "zipfille";
    }



    @RequestMapping(value = "/dowloand", method = RequestMethod.POST)
    public String zipFille(HttpServletResponse response) {

        Long [] fileList = photosForZip.keySet().toArray(new Long[photosForZip.size()]);
        convertFiileToZip (fileList);
        try {
            FileInputStream is = new FileInputStream(fileZip);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileZip.getName() + "\"");
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[is.available()];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            response.flushBuffer();
            is.close();
            os.close();
            fileZip.delete();
            photosForZip.clear();
        } catch (IOException x) {

            x.printStackTrace();

        }
        return "index";
    }

    private void convertFiileToZip (Long[] fileList){
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileZip));
            for (Long idd : fileList) {
                ByteArrayInputStream sourceStream = new ByteArrayInputStream(photosForZip.get(idd));
                out.putNextEntry(new ZipEntry(idd.toString()+".JPG"));
                int len;
                while ((len = sourceStream.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            }
            out.flush();
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}








