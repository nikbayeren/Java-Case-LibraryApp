# LibraryApp - Yayınevi Yönetim Sistemi

Bu proje, Spring Boot kullanılarak geliştirilmiş bir kütüphane yönetim sistemidir.

## Proje 

### Veri Modeli

- **Publisher:** publisherID, publisherName, establishmentYear, address, phone, email
- **Books:** id, title, price, isbn13, publish_year, authorID, publisherID
- **Author:** authorID, authorNameSurname, birthYear, country

## Kurulum

### Gereksinimler

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Veritabanı Kurulumu

1. PostgreSQL'de yeni bir veritabanı oluşturun:
```sql
CREATE DATABASE library_db;
```

2. `application.properties` dosyasındaki veritabanı bağlantı bilgilerini güncelleyin:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=postgres
spring.datasource.password=(password)
```

### Uygulama Çalıştırma

1. Projeyi klonlayın:
```bash
git clone <https://github.com/nikbayeren/Java-Case-LibraryApp>
cd Java-Case-LibraryApp
```

2. Maven ile bağımlılıkları indirin:
```bash
mvn clean install
```

3. Uygulamayı çalıştırın:
```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8088/swagger-ui/index.html` adresinde çalışacaktır.

# API Dokümantasyonu

API Docs: http://localhost:8088/api-docs

## Özellikler
- **CRUD İşlemleri:** Kitap, yazar ve yayınevi için ekleme, güncelleme, silme, listeleme.
- **Kitap eklerken yazar ve yayınevi ismiyle otomatik ilişkilendirme.
- **Özel Listeleme ve Filtreleme:**
  - Tüm yayınevleri, kitaplar, yazarlar.
  - İlk 2 yayınevi ve ilgili kitap/yazarları.
  - 'A' harfiyle başlayan kitaplar .
  - 2023 sonrası basılan kitaplar 
  - Fiyat aralığı, başlık/ISBN arama, yazar/yayınevine göre filtreleme.
- **Google Books API entegrasyonu:** Feign Client ile dış API'den kitap arama 
- **Unit Testler:** 2 adet unit test 
**PostgreSQL**: Veritabanı olarak PostgreSQL kullanımı
**Swagger Dokümantasyonu**: API'lerin otomatik dokümantasyonu

### Ekstra Güvenlik ve Doğrulama
- ISBN doğrulama ve benzersizlik kontrolü.
- Hatalı veri girişlerinde hata mesajları.

## Teknolojiler

- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- PostgreSQL
- Feign Client
- Swagger/OpenAPI 3
- JUnit 5
- Mockito

## API Endpoints

### Kitap İşlemleri
- `GET /api/books` - Tüm kitapları listele (sayfalı)
- `GET /api/books/all` - Tüm kitapları listele (tam liste)
- `GET /api/books/{id}` - ID'ye göre kitap getir
- `POST /api/books` - Yeni kitap oluştur (ID ile)
- `POST /api/books/create-with-names` - Yazar ve yayınevi ismiyle kitap ekle
- `PUT /api/books/{id}` - Kitap güncelle
- `DELETE /api/books/{id}` - Kitap sil
- `GET /api/books/starts-with-a` - A harfiyle başlayan kitaplar
- `GET /api/books/year/{year}` - Belirli yıldan sonra basılan kitaplar
- `GET /api/books/search-google?query=` - Google Books'ta ara
- `GET /api/books/search?query=` - Başlığa veya ISBN'e göre kitap ara
- `GET /api/books/publisher/{publisherId}` - Yayınevine göre kitaplar
- `GET /api/books/author/{authorId}` - Yazara göre kitaplar
- `GET /api/books/price?minPrice=&maxPrice=` - Fiyat aralığına göre kitaplar
- `GET /api/books/publishers/books?publisherIds=1&publisherIds=2` - İki yayınevine ait kitaplar

### Yayınevi İşlemleri
- `GET /api/publishers` - Tüm yayınevlerini listele
- `GET /api/publishers/{id}` - ID'ye göre yayınevi getir
- `POST /api/publishers` - Yeni yayınevi ekle
- `PUT /api/publishers/{id}` - Yayınevi güncelle
- `DELETE /api/publishers/{id}` - Yayınevi sil
- `GET /api/publishers/first2-with-books-and-authors` - İlk 2 yayınevi ve kitap/yazarlarını listele

### Yazar İşlemleri
- `GET /api/authors` - Tüm yazarları listele
- `GET /api/authors/{id}` - ID'ye göre yazar getir
- `POST /api/authors` - Yeni yazar ekle
- `PUT /api/authors/{id}` - Yazar güncelle
- `DELETE /api/authors/{id}` - Yazar sil

## Veritabanı Şeması

### Publisher (Yayınevi)
- `publisher_id` (PK)
- `publisher_name`

### Book (Kitap)
- `book_id` (PK)
- `title`
- `price`
- `isbn13`
- `publish_year`
- `publisher_id` (FK)

### Author (Yazar)
- `author_id` (PK)
- `author_name_surname`
- `book_id` (FK) // Her kitap bir yazara ait, bir yazarın birden fazla kitabı olabilir.

## Test Çalıştırma
Projede örnek controller ve service testleri bulunmaktadır. Testleri çalıştırmak için:

```bash
mvn test
```
Komutu ile tüm unit testler otomatik olarak çalışacaktır.



