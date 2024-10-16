package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class VNPayValidation {
    public static Map<String, String> validate(Map<String, String> params, HttpServletRequest request) {
        Map<String, String> error = new HashMap<>();

        if (params.get("vnp_TransactionStatus") == null) {
            return MapConfig.buildMapKey(error, "Transaction status is missing.");
        }
//
//        if (params.get("vnp_TxnRef") == null) {
//            errors.put("txn_ref_missing", "Transaction reference is missing.");
//        }
//
//        if (params.get("vnp_SecureHash") == null) {
//            errors.put("secure_hash_missing", "Secure hash is missing.");
//        }
//
//        // Xác thực tính toàn vẹn của chữ ký bằng cách tính toán lại vnp_SecureHash
//        String vnp_SecureHash = params.get("vnp_SecureHash");
//        String calculatedHash = calculateSecureHash(params);  // Hàm tính toán chữ ký
//        if (!vnp_SecureHash.equals(calculatedHash)) {
//            errors.put("invalid_secure_hash", "Secure hash is invalid.");
//        }
//
//        // Kiểm tra trạng thái giao dịch
//        String transactionStatus = params.get("vnp_TransactionStatus");
//        if (!"00".equals(transactionStatus)) {
//            errors.put("transaction_failed", "Transaction failed with status: " + transactionStatus);
//        }
//
//        // Có thể thêm các kiểm tra bổ sung nếu cần (ví dụ: kiểm tra thời gian)
//        String transactionTime = params.get("vnp_PayDate");
//        if (!validateTransactionTime(transactionTime)) {
//            errors.put("invalid_transaction_time", "Transaction time is invalid.");
//        }
//
//        return errors;
//    }
//
//    // Hàm tính toán lại chữ ký để so sánh với vnp_SecureHash
//    private static String calculateSecureHash(Map<String, String> params) {
//        // Loại bỏ vnp_SecureHash khỏi danh sách các tham số để không tính vào hash
//        params.remove("vnp_SecureHash");
//        List<String> fieldNames = new ArrayList<>(params.keySet());
//        Collections.sort(fieldNames);
//
//        StringBuilder hashData = new StringBuilder();
//        for (String fieldName : fieldNames) {
//            String fieldValue = params.get(fieldName);
//            if (fieldValue != null && !fieldValue.isEmpty()) {
//                hashData.append(fieldName).append('=').append(fieldValue);
//                hashData.append('&');
//            }
//        }
//        // Xóa '&' cuối cùng
//        hashData.deleteCharAt(hashData.length() - 1);
//
//        return VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
//    }
//
//    // Hàm kiểm tra thời gian giao dịch
//    private static boolean validateTransactionTime(String transactionTime) {
//        // Bạn có thể thêm logic kiểm tra thời gian của giao dịch ở đây, ví dụ:
//        // So sánh thời gian giao dịch với thời gian hiện tại để đảm bảo không quá hạn
//        if (transactionTime == null || transactionTime.isEmpty()) {
//            return false;
//        }
//
//        // Bạn có thể tùy chỉnh cách kiểm tra thời gian, ví dụ như kiểm tra xem thời gian có hợp lệ không
//        return true;  // Trả về true nếu hợp lệ
        return error;
    }
}
