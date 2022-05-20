package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.entity.course.Course;
import lk.esofttopup.chatbot.entity.course.CourseContent;
import lk.esofttopup.chatbot.entity.course.Faculty;
import lk.esofttopup.chatbot.repository.FacultyRepository;
import lk.esofttopup.chatbot.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CourseDataService {
    private final FacultyRepository facultyRepository;

    @PostConstruct
    @Transactional
    void loadDataFromExcel() throws IOException, InvalidFormatException {
        InputStream resource = getClass().getResourceAsStream(File.separator+"data"+File.separator+"Course_Details.xlsx");
        Workbook workbook = CommonUtil.loadExcelFile(resource);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, List<String>> facultyCourseMap = new HashMap<>();
        Map<String, List<CourseContent>> courseContentMap = new HashMap<>();
        Map<String, Course> courseNameVsCourseMap = new HashMap<>();
        //Map<String, Course> facultyNameVsCourseMap = new HashMap<>();

        int i = 1;
        String facultyMark = "";
        String courseMark = "";
        String durationMark = "";
        String courseFeeMark = "";
        //String currencyMark = "";
        String uniFeeMark = "";
        String courseContentMark = "";
        for (Row row : sheet) {
            if (i == 1) {
                i++;
                log.info("Skip first row for headers...");
                continue;
            }

            log.info("Reading row number : {} ", i);

            String facultyName = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
            String courseName = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            String duration = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
            String courseFee = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
            String uniFee = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
            //String currency = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
            String courseContent = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";


            if (StringUtils.isNotEmpty(facultyName)) {
                facultyMark = facultyName;
            }
            if (StringUtils.isNotEmpty(courseName)) {
                courseMark = courseName;
            }
            if (StringUtils.isNotEmpty(courseFee)) {
                courseFeeMark = courseFee;
            }
            if (StringUtils.isNotEmpty(duration)) {
                durationMark = duration;
            }
            if (StringUtils.isNotEmpty(courseContent))
                courseContentMark = courseContent;

            if (StringUtils.isNotEmpty(uniFee))
                uniFeeMark = uniFee;

            log.info("Data going to be updated faculty :{} course :{} course Fee :{} Duration :{} Uni Fee :{} Course Content :{}", facultyMark, courseMark, courseFeeMark, durationMark, uniFeeMark, courseContentMark);



            Course course = new Course();
            course.setName(courseMark);
            course.setCourseFee(courseFeeMark);
            course.setDuration(durationMark);
            course.setUniversityFee(uniFeeMark);

            if (!courseNameVsCourseMap.containsKey(courseMark)) {
                courseNameVsCourseMap.put(courseMark, course);
            }

            List<CourseContent> orDefault = courseContentMap.getOrDefault(courseMark, new ArrayList<CourseContent>());
            orDefault.add(new CourseContent(courseContentMark));
            courseContentMap.put(courseMark, orDefault);

            //Faculty vs Course
            List<String> orDefault1 = facultyCourseMap.getOrDefault(facultyMark, new ArrayList<>());
            orDefault1.add(courseMark);
            facultyCourseMap.put(facultyMark, orDefault1);

            i++;

        }
        courseContentMap.keySet().forEach(key -> log.info("Course :{} values :{}", key, courseContentMap.get(key)));
       List<Faculty> faculties=new ArrayList<>();
        facultyCourseMap.keySet().forEach(fac -> {
            Faculty faculty=new Faculty();
            faculty.setName(fac);
            // remove duplicates from list
            List<String> uniqueCourseStrList = facultyCourseMap.get(fac).stream().distinct().collect(Collectors.toList());

            List<Course> courses=new ArrayList<>();
            uniqueCourseStrList.stream().forEach(courseName->{
                // get course details
                Course courseDetails = courseNameVsCourseMap.get(courseName);

                Course course=new Course();
                course.setName(courseName);
                course.setCourseFee(courseDetails.getCourseFee());
                course.setDuration(courseDetails.getDuration());
                course.setUniversityFee(courseDetails.getUniversityFee());
                course.setContentList(courseContentMap.get(courseName));
                courses.add(course);
            });
            faculty.setCourseList(courses);
            faculties.add(faculty);
        });
        facultyRepository.saveAll(faculties);
    }
}
