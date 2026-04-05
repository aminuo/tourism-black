package com.example.tourismblack.repository;

import java.util.List;

import com.example.tourismblack.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Integer> {
        // JpaRepository已经提供了基本的CRUD操作，包括查询全部数据的方法

        // 按标题模糊查询
        List<ScenicSpot> findByTitleContaining(String title);

        // 通过三级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code WHERE c3.id = :c3Id", nativeQuery = true)
        List<ScenicSpot> findByC3Id(@Param("c3Id") Integer c3Id);

        // 通过标题和三级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code WHERE s.title LIKE %:title% AND c3.id = :c3Id", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndC3Id(@Param("title") String title, @Param("c3Id") Integer c3Id);

        // 通过标题和属性标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag_property stp ON s.id = stp.scenic_id JOIN tag_property p ON stp.property_code = p.code WHERE s.title LIKE %:title% AND p.id = :propertyId", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndPropertyId(@Param("title") String title,
                        @Param("propertyId") Integer propertyId);

        // 通过一级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code JOIN tag_category2 c2 ON c3.c2_code = c2.code JOIN tag_category1 c1 ON c2.c1_code = c1.code WHERE c1.id = :c1Id", nativeQuery = true)
        List<ScenicSpot> findByC1Id(@Param("c1Id") Integer c1Id);

        // 通过二级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code JOIN tag_category2 c2 ON c3.c2_code = c2.code WHERE c2.id = :c2Id", nativeQuery = true)
        List<ScenicSpot> findByC2Id(@Param("c2Id") Integer c2Id);

        // 通过标题和一级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code JOIN tag_category2 c2 ON c3.c2_code = c2.code JOIN tag_category1 c1 ON c2.c1_code = c1.code WHERE s.title LIKE %:title% AND c1.id = :c1Id", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndC1Id(@Param("title") String title, @Param("c1Id") Integer c1Id);

        // 通过标题和二级标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id JOIN tag_category3 c3 ON st3.tag3_code = c3.code JOIN tag_category2 c2 ON c3.c2_code = c2.code WHERE s.title LIKE %:title% AND c2.id = :c2Id", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndC2Id(@Param("title") String title, @Param("c2Id") Integer c2Id);

        // 通过属性标签ID搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag_property stp ON s.id = stp.scenic_id JOIN tag_property p ON stp.property_code = p.code WHERE p.id = :propertyId", nativeQuery = true)
        List<ScenicSpot> findByPropertyId(@Param("propertyId") Integer propertyId);

        // 通过三级标签code搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id WHERE st3.tag3_code = :tag3Code", nativeQuery = true)
        List<ScenicSpot> findByTag3Code(@Param("tag3Code") String tag3Code);

        // 通过属性标签code搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag_property stp ON s.id = stp.scenic_id WHERE stp.property_code = :propertyCode", nativeQuery = true)
        List<ScenicSpot> findByPropertyCode(@Param("propertyCode") String propertyCode);

        // 通过标题和三级标签code搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag3 st3 ON s.id = st3.scenic_id WHERE s.title LIKE %:title% AND st3.tag3_code = :tag3Code", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndTag3Code(@Param("title") String title,
                        @Param("tag3Code") String tag3Code);

        // 通过标题和属性标签code搜索景点
        @Query(value = "SELECT DISTINCT s.* FROM scenic_spots s JOIN scenic_tag_property stp ON s.id = stp.scenic_id WHERE s.title LIKE %:title% AND stp.property_code = :propertyCode", nativeQuery = true)
        List<ScenicSpot> findByTitleContainingAndPropertyCode(@Param("title") String title,
                        @Param("propertyCode") String propertyCode);
}
