# demo-payment


Bước1: validate  cơ bản như mô tả

Bước 2: Đẩy dữ liệu lên queue

Bước 3: Viết Project consumer data , insert data vào database

Bước 4: Sau khi insert db thành công , đẩy thông tin vào api sau 

https://api.foodbook.vn/ipos/ws/xpartner/callback/vnpay

Bước 5: Nhận phản hồi từ api của đối tác , Phản hồi lại request ban đầu
