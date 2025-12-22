@REM Tắt echo lệnh trên màn hình. Giúp cửa sổ CMD sạch đẹp hơn khi chạy.
@echo off
echo [ Preparing output folder...]
@REM nếu thư mục bin không tồn tại, tạo nó
if not exist bin mkdir bin 

@REM Hiển thị thông báo
echo [ Finding all Java files...]
@REM Bật enabledelayedexpansion để sử dụng biến cập nhật động trong vòng lặp
setlocal enabledelayedexpansion
@REM Tạo biến FILES để chứa danh sách tất cả file .java
set FILES=
@REM duyệt đệ quy qua tất cả các file .java trong thư mục hiện tại và các thư mục con
for /r %%f in (*.java) do (
    set FILES=!FILES! "%%f"
)
@REM  FILES chứa chuỗi "file1.java" "file2.java" "file3.java" ...

echo [ Compiling...]
@REM Biên dịch tất cả file .java trong FILES, xuất file .class vào thư mục bin
@REM javac: Trình biên dịch Java
@REM -d bin: Xuất file .class vào thư mục bin
javac -d bin %FILES%

@REM Kiểm tra nếu có lỗi khi biên dịch (errorlevel 1 nghĩa là lỗi) thì thông báo lỗi, dừng lại và không chạy tiếp 
if errorlevel 1 (
    echo  Compilation failed!
    pause
    exit /b
)
@REM nếu không có lỗi thông báo đã compile xong và chạy chương trình
@REM java: Trình thông dịch Java
@REM Chạy chương trình với lớp chính là megaman.userinterface.GameFrame
@REM -cp bin: Chạy với classpath là thư mục bin
@REM megaman.userinterface.GameFrame: class có hàm main
@REM pause: Đợi người dùng nhấn phím để xem kết quả
echo [ Compilation successful. Running...]
java -cp bin megaman.userinterface.GameFrame
pause



