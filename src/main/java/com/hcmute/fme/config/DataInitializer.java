package com.hcmute.fme.config;

import com.hcmute.fme.entity.*;
import com.hcmute.fme.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(
            UserRepository userRepository,
            BannerRepository bannerRepository,
            NewsRepository newsRepository,
            ProgramRepository programRepository,
            ScheduleRepository scheduleRepository) {

        return args -> {
            // Initialize users if empty
            if (userRepository.count() == 0) {
                log.info("Initializing default users...");

                User admin = User.builder()
                        .name("Admin Sinh viên")
                        .email("23146053@student.hcmute.edu.vn")
                        .password(passwordEncoder.encode("admin123"))
                        .studentId("23146053")
                        .role(User.Role.ADMIN)
                        .isActive(true)
                        .build();

                User student = User.builder()
                        .name("Nguyễn Văn A")
                        .email("20190001@student.hcmute.edu.vn")
                        .password(passwordEncoder.encode("student123"))
                        .studentId("20190001")
                        .role(User.Role.USER)
                        .isActive(true)
                        .build();

                userRepository.saveAll(List.of(admin, student));
                log.info("Created {} default users", 2);
            }

            // Initialize banners if empty
            if (bannerRepository.count() == 0) {
                log.info("Initializing default banners...");

                List<Banner> banners = List.of(
                        Banner.builder().imageUrl("/banner_1.jpg").displayOrder(1).isActive(true).build(),
                        Banner.builder().imageUrl("/banner_2.jpg").displayOrder(2).isActive(true).build(),
                        Banner.builder().imageUrl("/banner_3.jpg").displayOrder(3).isActive(true).build()
                );

                bannerRepository.saveAll(banners);
                log.info("Created {} default banners", banners.size());
            }

            // Initialize news if empty
            if (newsRepository.count() == 0) {
                log.info("Initializing default news...");

                List<News> newsList = List.of(
                        News.builder()
                                .title("RECAP | BUỔI BỐC THĂM CHÍNH THỨC CỦA 'FRESHMAN CHAMPION 2025'")
                                .category("Sự kiện")
                                .publishedDate("02/10/2025")
                                .imageUrl("/new_1.jpg")
                                .url("https://www.facebook.com/share/19hqTid6UC/")
                                .isTrending(true)
                                .build(),
                        News.builder()
                                .title("TIÊU CHUẨN XÉT CHỌN DANH HIỆU 'SINH VIÊN 5 TỐT' - CẤP TRUNG ƯƠNG")
                                .category("Đào tạo")
                                .publishedDate("05/09/2025")
                                .imageUrl("/new_2.jpg")
                                .url("https://www.facebook.com/share/19d4Th57Un/")
                                .isTrending(false)
                                .build(),
                        News.builder()
                                .title("HCMUTE CHÀO MỪNG NĂM HỌC MỚI 2025-2026")
                                .category("Tin tức")
                                .publishedDate("15/08/2025")
                                .imageUrl("/new_3.jpg")
                                .url("https://www.facebook.com/share/1HMBdygYWd/")
                                .isTrending(true)
                                .build()
                );

                newsRepository.saveAll(newsList);
                log.info("Created {} default news", newsList.size());
            }

            // Initialize programs if empty
            if (programRepository.count() == 0) {
                log.info("Initializing default programs...");

                List<Program> programs = List.of(
                        // Undergraduate programs
                        Program.builder().name("Kỹ thuật Cơ điện tử").code("mechatronics").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        Program.builder().name("Kỹ thuật Cơ khí").code("mechanicalEngineering").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        Program.builder().name("Công nghệ Chế tạo máy").code("manufacturing").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        Program.builder().name("Kỹ thuật Công nghiệp").code("industrialEngineering").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        Program.builder().name("Công nghệ Kỹ thuật Chế biến và Bảo quản Nông sản Thực phẩm").code("woodAndFurniture").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        Program.builder().name("Kỹ thuật Robot và Trí tuệ nhân tạo").code("roboticsAI").type(Program.ProgramType.UNDERGRADUATE).isActive(true).build(),
                        // Postgraduate programs
                        Program.builder().name("Thạc sĩ").code("masters").type(Program.ProgramType.POSTGRADUATE).isActive(true).build(),
                        Program.builder().name("Tiến sĩ").code("doctorate").type(Program.ProgramType.POSTGRADUATE).isActive(true).build()
                );

                programRepository.saveAll(programs);
                log.info("Created {} default programs", programs.size());
            }

            // Initialize sample schedules if empty
            if (scheduleRepository.count() == 0) {
                log.info("Initializing sample schedules...");

                LocalDate today = LocalDate.now();
                List<Schedule> schedules = List.of(
                        Schedule.builder()
                                .date(today)
                                .shift("Sáng (7:30 - 11:30)")
                                .studentName("Nguyễn Văn A")
                                .studentEmail("20190001@student.hcmute.edu.vn")
                                .isConfirmed(false)
                                .build(),
                        Schedule.builder()
                                .date(today.plusDays(1))
                                .shift("Chiều (13:30 - 17:30)")
                                .studentName("Trần Thị B")
                                .studentEmail("20190002@student.hcmute.edu.vn")
                                .isConfirmed(false)
                                .build(),
                        Schedule.builder()
                                .date(today.plusDays(3))
                                .shift("Tối (17:30 - 21:30)")
                                .studentName("Lê Văn C")
                                .studentEmail("20190003@student.hcmute.edu.vn")
                                .isConfirmed(false)
                                .build()
                );

                scheduleRepository.saveAll(schedules);
                log.info("Created {} sample schedules", schedules.size());
            }

            log.info("Data initialization completed!");
        };
    }
}
