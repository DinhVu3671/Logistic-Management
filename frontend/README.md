# Giao diện Frontend của ứng dụng hỗ trợ lập lịch giao hàng

----
 

## Quản lý thông tin cơ bản

  

*Là chức năng các thông tin cơ bản của bài toán, bao gồm thông tin về khách hàng, kho hàng, đơn hàng và đội xe*

  

  

1.  **Quản lý khách hàng** :

  

- Lưu, thêm, sửa, xóa và tìm kiếm các thông tin về các khách hàng.
  

- Giao diện trang quản lý khách hàng:

  
![alt](/images/customer.png)

  

-  *STT* : số thứ tự của khách hàng.

-  *Tên khách hàng* : tên của khách hàng, có thể dùng địa chỉ của khách hàng như là tên khách hàng.

-  *Mã khách hàng* : mã của khách hàng dùng để định danh khách hàng trong hệ thống, mỗi khách hàng có một mã khác nhau.
  

-  *Vĩ độ* : tọa độ thực tế - vĩ độ của khách hàng trong yêu cầu, có thể lấy ra bằng cách định vị địa chỉ của khách hàng trên google map.

  

-  *Kinh độ* : tọa độ thực tế - kinh độ của khách hàng trong yêu cầu, có thể lấy ra bằng cách định vị địa chỉ của khách hàng trên google map.

  

-  *Địa chỉ* : Địa chỉ thực tế của khách hàng.

  

-  *Thời điểm mở* : Thời điểm mà điểm khách hàng này bắt đầu mở cửa hoạt động (xe cần tới điểm khách hàng này sau thời điểm này).

  

-  *Thời điểm đóng* : Thời điểm muộn nhất mà điểm khách hàng này còn mở cửa hoạt động (xe cần tới điểm khách hàng này trước thời điểm này).

  

-  *Thao tác* : Có thể xem thông tin chi tiết khách hàng, xóa hoặc chỉnh sửa thông tin của khách hàng.

  

  

**Chú ý**:

  

- Mỗi khách hàng có một mã duy nhất.

  

  

2.  **Quản lý kho hàng** :

  

  

- Lưu thông tin về các điểm kho của nhà cung cấp, thông tin của mỗi điểm kho là một hàng trong danh sách.

  

- Giao diện trang quản lý kho hàng:

  

 ![alt](/images/depot.png)
  
  

-  *STT* : số thứ tự của kho hàng.

-  *Tên kho* : tên của điểm kho, có thể dùng địa chỉ của điểm kho như là tên điểm kho.

-  *Mã kho* : Mã của điểm kho, dùng để định danh điểm kho trong hệ thống.
  

-  *Vĩ độ* : tọa độ thực tế - vĩ độ của điểm kho, có thể lấy ra bằng cách định vị địa chỉ của điểm kho trên google map.

  

-  *Kinh độ* : tọa độ thực tế - kinh độ của điểm kho, có thể lấy ra bằng cách định vị địa chỉ của điểm kho trên google map.

  

-  *Thời điểm mở* : Thời điểm mà điểm kho này bắt đầu mở cửa hoạt động (xe cần xuất phát tại kho sau thời điểm này).

  

-  *Thời điểm đóng* : Thời điểm muộn nhất mà điểm kho này còn mở cửa hoạt động (xe cần về tới điểm kho trước thời điểm này).

  

-  *Thao tác* : Có thể xem thông tin chi tiết kho hàng, xóa hoặc chỉnh sửa thông tin của kho hàng.
  

**Chú ý**:

  

- Mã kho hàng là duy nhất.

  

3.  **Quản lý đội xe** :

  

- Lưu thông tin về đội xe của nhà cung cấp, mỗi một hàng trong danh sách là thông tin về một xe.

  

- Giao diện quản lý xe:

![alt](/images/vehicle.png)
  

-  *STT* : số thứ tự của xe.

  

-  *Tên xe* : tên của xe trong đội xe.

  

-  *Sức chứa* : Khối lượng hàng lớn nhất và tổng dung tích hàng lớn nhất mà xe có thể tải.

  

-  *Phí di chuyển* : Chi phí cho việc di chuyển của xe, tính trên đơn vị VND/km.

  

-  *Vận tốc trung bình* : Vận tốc trung bình của xe, dùng để tính ra thời gian di chuyển của xe.

  

-  *Tên lái xe* : Tên lái xe chính của xe này.

-  *Loại xe* : Qui định xe thuộc loại xe nào.
- *Sẵn sàng*: Biểu thị tình trạng hiện tại của xe, tức là xe có sẵn sàng phục vụ hay không.
-  *Thao tác* : Có thể xem thông tin chi tiết về xe, xóa hoặc chỉnh sửa thông tin của xe.
  

**Chú ý**:

  

- Số lượng record trong danh sách vehicles chính là số lượng xe của bài toán (đây là một điều kiện ràng buộc của bài toán).

  

- Các xe trong bài toán có sức chứa là khác nhau.

4.  **Quản lý đơn hàng** :

  

  

- Lưu thông tin về các đơn hàng của các khách hàng, thông tin của mỗi đơn là một hàng trong danh sách.

  

- Giao diện trang quản lý đơn hàng:

  

 ![alt](/images/order.png)
  
  

-  *STT* : số thứ tự của đơn hàng.

-  *Mã đơn hàng* : Mã của đơn hàng, dùng để định danh đơn hàng trong hệ thống.

-  *Mã khách hàng* : Mã của điểm khách hàng, xác định khách hàng nào yêu cầu đơn hàng này.
  

-  *Giá trị đơn hàng* : Tổng giá trị của đơn hàng đã đặt.

  

-  *Khối lượng* : Tổng khối lượng của đơn hàng.


-  *Thể tích* : Tổng thể tích của đơn hàng.

  

-  *Chế độ giao hàng* : Nếu khách hàng muốn giới hạn thời điểm giao hàng thì có thể qui định lại giờ giao hàng thông qua thay đổi chế độ giao hàng.

  

-  *Số loại sản phẩm* : Số loại sản phẩm được ghép vào cùng đơn hàng.
  
-  *Thao tác* : Có thể xem thông tin chi tiết về đơn hàng, xóa hoặc chỉnh sửa thông tin của đơn hàng.

**Chú ý**:

  

- Mã đơn hàng là duy nhất.
- Chế độ giao hàng mặc định là tiêu chuẩn.

## Khởi tạo lộ trình

*Là chức năng chính của hệ thống, trong một khoảng thời gian cho phép thực hiện xây dựng một giải pháp là lộ trình của các xe sao cho tổng chi phí là tối ưu, gồm có 3 bước: Cấu hình thuật toán, Phân cụm khách hàng và Xác nhận khởi tạo*

1.  **Cấu hình thuật toán** :
 
 - Trên màn hình giao diện gồm có 2 nhóm thông số là:
 	- cơ bản: gồm các thông số cơ bản đối với người dùng như là: giới hạn thời gian chạy của xe, số km xe chạy trong ngày.
	- nâng cao: gồm các thông số liên quan trực tiếp đến cấu hình giải thuật di truyền như là: kích thước của quần thể, số lượng thế hệ, ...

 ![alt](/images/config.png)

2.  **Phân cụm khách hàng** :

- Trên màn hình giao diện là danh sách các khách hàng đã được hệ thống phân cụm (mặc định là phân cụm theo địa lý). Người dùng có thể tùy chỉnh lại phân cụm của các khách hàng về kho theo mong muốn.

 ![alt](/images/cluster.png)

3.  **Xác nhận khởi tạo** :

- Màn hình xác nhận người dùng có muốn khởi tạo một lộ trình cho các xe theo các thông tin cơ bản của bài toán đã được cấu hình từ bước 1 và bước 2. Người dùng có thể bấm hủy bỏ để quay về bước 1.

 ![alt](/images/confirm.png)

----

## Hướng dẫn chạy thử
- Install npm or yarn(recommend): Install via Chocolatey on window

	`choco install yarn`

- In the front-end project directory, you can run command below to start client:

	`yarn start`

