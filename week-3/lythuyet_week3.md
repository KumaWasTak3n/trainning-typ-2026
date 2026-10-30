# Khái niệm
- Authentication (Xác thực) là quá trình kiểm tra, xác thực danh tính của một người (trả lời cho câu hỏi "bạn là ai). Chỉ khi đảm bảo hợp lệ thì mới sang bước tiếp theo.
- Authorization (Phân quyền) là quá trình sau khi *Xác thực* có được truy cập vào một tài nguyên hay thực hiênj hành động cụ thể được không.
- Phân biệt: Xác thực là kiểm tra người dùng có hợp lệ hay không còn Phân quyền kiểm tra xem người dùng hợp lệ đó có được quyền gì.
# Phân biệt cơ chế xác thực
## Session-based:
server nhớ user thông qua bộ nhớ của nó.
### Flow hoạt động
Client gửi username/password lên Server -> Kiểm tra đúng thông tin -> Tạo Session lưu trong bộ nhớ Server và sinh ra một chuỗi mã ngẫu nghiên là SessionID -> Server gửi SessionId về Client qua HTTP Header -> Các Request tiếp theo sẽ được đính kèm Cookie bao gồm SessionID -> Server nhận được SessionID và mang đi tra cứu bộ nhớ, nếu còn hạn thì cho quyền truy cập -> Server xoá Session ra khỏi bộ nhớ, đồng thời yêu cầu Client khoá Cookie
**Stateful:** Server phải duy trì trạng thái của tất cả user đang đăng nhập.

### HTTP Cookie
là những đoạn dữ liệu nhỏ dạng văn bản do Server tạo ra, gửi xuống và lưu trữ trên Client
## Token-based
Chủ yếu dùng JWT, client dữ token do server để tự chứng minh danh tính.
### Flow hoạt động
Client gửi username/password lên Server -> Kiểm tra đúng thông tin -> Server tạo ra một token cho Client, Client tự lưu token này -> Các Request tiếp theo Client sẽ đính kèm token vào header của HTTP Request -> Server nhận được Request, lấy token và dùng Secret Key của mình để xác minh chữ ký, nếu đúng và chưa hết hạn, server cho phép truy cập mà k cần DB kiểm tra -> Client chỉ cần xoá Token ở phía mình.
**Stateless:** Server không lưu trạng thái đăng nhập, cứ có Token hợp lệ là tin tưởng.
## Phân biệt
Session-based tốn chi phí hơn Token-based vì Server phải lưu trữ và kiểm tra lại. Session-based cũng khó mở rộng vì có thể gặp trường hợp Server này biết SessionID mà Server kia không biết đối với nhiều Server chạy đồng thời. Nhưng Session-based lại kiểm soát phiên tốt hơn vì Token-Based khi đã phát thì có hiệu lực đến khi hết hạn, trong đó Session-based chỉ cần kick ra ngay là được bằng cách xoá Session trên server.
![[Pasted image 20261024173918.png]]
![[Pasted image 20261024174312.png]]
# JWT (JSON Web Token)
JWT quy định truyền tải thông tin an toàn giữa các bên dưới dạng JSON. JWT đóng vai trò cho cơ chế xác thực Token.
JWT chỉ uỷ quyền chứ không xác thực
## Cấu trúc
Một chuỗi JWT được chia làm 3 phần, tách nhau bởi dấu . có dạng *Header.Payload.Signature*
1. Header
	Chứa thông tin về Token (mặc định: JWT) và thuật toán mã hoá được sử dụng để tạo chữ ký.
2. Payload
	Chứa các "Claims" (những tuyên bố, thông tin) về người dùng và các siêu dữ liệu khác. Có 3 loại Claims chính:
		- Registered claims: Các trường được định nghĩa sẵn bởi chuẩn JWT để hỗ trợ hệ thống. Ví dụ: `iss` (người phát hành), `exp` (thời gian hết hạn), `sub` (ID của người dùng).
		- Public claims: Các thông tin tự định nghĩa (thường được đăng ký qua URI để tránh trùng lặp).
		- Private claims: Dữ liệu riêng ứng dụng của bạn cần truyền đi, ví dụ: `role: "admin"`, `email: "user@example.com"`.
3. Signature (chữ ký)
	Được tạo bằng cách lấy Header và Payload (mã hoá Base64Url) kết hợp với chuỗi secret key và qua giai đoạn mã hoá ở header. Chữ ký này là phần quan trọng nhất để giúp server xác minh qua Token là hàng thật và dữ liệu chưa bị hacker sửa đổi trên đường truyền.
## Lợi ích
- Dễ mở rộng: chỉ cần Secret Key chung là có thể quyền vào bất kỳ server nào có chung secret key.
- Tiết kiệm chi phí: Server không phải lưu Session, giúp tiết kiệm phần cứng.
- Đa nền tảng: Dùng chung một cơ chế xác thực
## Hạn chế và rủi ro bảo mật
- Kiểm soát phiên: server chỉ kiểm tra được token có còn hạn thôi hay không, nếu muốn ép đăng xuất thì nên dùng Session-based vì Server sẽ lưu thuông tin từng thằng một.
# Password Hashing
*Mục tiêu của bảo mật không phải là miễn nhiễm với các cuộc tấn công (điều này là không thể, mà làm chậm kẻ tấn công đủ để chúng chuyển sự chú ý sang những nơi khác*
## Tại sao không lưu password dưới dạng plain text
Nếu DB bị đánh cắp, dữ liệu của người dùng sẽ bị rò rỉ ra ngoài, toàn bộ tài khoản sẽ bị chiếm quyền quyển soát ngay lập tức.
## Tại sao không dùng MD5/SHA? (brute force, rainbow table)
Ta không dùng MD5/SHA-1 vì 
- tấn công từ điển kiểu phỏng đoán (Rainbow table): Vì người dùng thường dùng các mật khẩu giống nhau như "123456", "password", ... nên các hacker có thể dùng một chuỗi mật khẩu này để tạo ra các mã hash rồi tra cứu DB xem có bao nhiêu bản ghi trùng.
- Tấn công bruteforce: các hacker liên tục thử các mật khẩu từ mật khẩu yếu dễ, đoán đến các tổ hợp ký tự. Phương pháp này sẽ dễ khai thác vào  các hệ thống không giới hạn số lần thử.
## Dùng bcrypt/argon2: salt, cost factor
### Salting
salting thay vì hashing từng password chúng ta có thể sinh salt ngẫu nghiên vào đầu chuỗi trước khi băm.
*Salt + Password* -> Hashing
Trong cơ sở dữ liệu ta sẽ lưu trữ trường salt cạnh trường password.
Việc Salt cho ta đỡ bị tấn công bằng Rainbow Table.

Vẫn có nhược điểm là bị brute force dùng GPU để tính toán.
### Slow Hashing/Cost factor
Các thuật toán bcrypt, scrypt, argon2 là các thuật toán chạy chậm, tốn hiệu năng và bộ nhớ.
Ta có cấu trúc điển hình bcrypt mà em định dùng trong dự án:
(prefix)-(work-factor)-(22 kí tự salt)-(31 ký tự hashing)
Work factor hay cost factor là tham số quyết định thuật toán chạy chậm đến mức nào.
Thuật toán sẽ chạy chậm theo cấp số nhân dựa theo work factor, một work factor mà để là 1 thì hàm sẽ chạy chậm gấp 2. thường work factor sẽ để là 16.