package ru.fisher.VehiclePark.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fisher.VehiclePark.util.NPlusOne;

@Hidden
@Tag(name = "Контроллер для теста N+1",
        description = "Тест по решению проблемы N+1")
@RestController
@RequestMapping("/nplusone")
@RequiredArgsConstructor
public class NPlusOneDemoController {

    @Autowired
    private final NPlusOne nPlusOne;
    private final EntityManager entityManager;

    private void clearHibernateCache() {
        entityManager.clear(); // Очищаем кеш первого уровня
        Cache cache = entityManager.getEntityManagerFactory().getCache();
        cache.evictAll(); // Очищаем кеш второго уровня
    }

    @GetMapping("/compare")
    public ResponseEntity<String> compareApproaches() {
        StringBuilder result = new StringBuilder();

        clearHibernateCache();
        long startBad = System.nanoTime();
        nPlusOne.demonstrateNPlusOneProblem(1L);
        long endBad = System.nanoTime();

        clearHibernateCache();
        long startGood = System.nanoTime();
        nPlusOne.demonstrateEntityGraphSolution(1L);
        long endGood = System.nanoTime();

        result.append("Время с N+1: ").append((endBad - startBad) / 1_000_000).append(" ms;\n");
        result.append("Время с EntityGraph: ").append((endGood - startGood) / 1_000_000).append(" ms;\n");
        result.append("Улучшение: ").append(100 - (endGood - startGood)*100/(endBad - startBad)).append("% быстрее");

        return ResponseEntity.ok(result.toString().trim());
    }

    @GetMapping("/bad")
    public void triggerNPlusOne() {
        long start = System.nanoTime();
        nPlusOne.demonstrateNPlusOneProblem(1L);
        long end = System.nanoTime();
        System.out.println("⏱ Время с N+1: " + (end - start) / 1_000_000 + " мс");
    }

    @GetMapping("/good")
    public void triggerEntityGraph() {
        long start = System.nanoTime();
        nPlusOne.demonstrateEntityGraphSolution(1L);
        long end = System.nanoTime();
        System.out.println("✅ Время с EntityGraph: " + (end - start) / 1_000_000 + " мс");
    }

}
