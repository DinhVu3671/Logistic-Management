# Backend của ứng dụng hỗ trợ lập lịch giao hàng

----
## Mô tả

Mã nguồn của server được tạo trong dự án VRP Project. Dự án bao gồm hai module chính là: module vrp-core và module vrp-preprocess. 
- Module vrp-core có nhiệm vụ chứa thuật toán tối ưu lộ trình (giải thuật di truyền) được xây dựng như một thư viện mã nguồn mở giúp có thể dễ dàng tái sử dụng bằng cách import file .jar. 
- Module vrp-preprocess có vai trò là web-service, cung cấp các API cho phía client, xử lý dữ liệu và tiền xử lý dữ liệu trước khi chạy giải thuật di truyền ở module vrp-core nhằm mục đích tạo lộ trình tối ưu

## Hướng dẫn chạy thử
- Di chuyển đến thư mục back-end, mở project và chạy bằng các IDE Java như IntelliJ hoặc Eclipse.
