public class HttpBattleground {

    public static void main(String[] args) {
        System.out.println("=== HTTP BATTLEGROUND: HTTP Fundamentals ===\n");
        httpMethods();
        statusCodes();
        headers();
    }

    private static void httpMethods() {
        System.out.println("--- HTTP Methods ---\n");

        String[][] methods = {
            {"GET", "Retrieve data. Does NOT modify server state. Safe and idempotent.", "GET /api/users/1"},
            {"POST", "Create a new resource. Not idempotent (multiple calls create multiple resources).", "POST /api/users"},
            {"PUT", "Replace an entire resource. Idempotent (same result on repeat calls).", "PUT /api/users/1"},
            {"PATCH", "Partially update a resource. Only changes specified fields.", "PATCH /api/users/1"},
            {"DELETE", "Remove a resource. Idempotent.", "DELETE /api/users/1"}
        };

        for (String[] m : methods) {
            System.out.println("Method: " + m[0]);
            System.out.println("  Purpose: " + m[1]);
            System.out.println("  Example: " + m[2]);
            System.out.println();
        }
    }

    private static void statusCodes() {
        System.out.println("--- HTTP Status Codes ---\n");

        String[][] codes = {
            {"200", "OK", "Request succeeded."},
            {"201", "Created", "Resource created successfully (after POST)."},
            {"204", "No Content", "Success but no body in response (after DELETE)."},
            {"301", "Moved Permanently", "Resource has a new permanent URL."},
            {"400", "Bad Request", "Client sent invalid data."},
            {"401", "Unauthorized", "Authentication required or failed."},
            {"403", "Forbidden", "Authenticated but not allowed to access."},
            {"404", "Not Found", "Resource does not exist."},
            {"409", "Conflict", "Conflict with current state (e.g., duplicate)."},
            {"500", "Internal Server Error", "Server crashed or unexpected error."},
            {"502", "Bad Gateway", "Upstream server returned invalid response."},
            {"503", "Service Unavailable", "Server is overloaded or down for maintenance."}
        };

        for (String[] c : codes) {
            System.out.println("  " + c[0] + " " + c[1] + " - " + c[2]);
        }
        System.out.println();
    }

    private static void headers() {
        System.out.println("--- HTTP Headers ---\n");
        System.out.println("Headers carry metadata for request and response.\n");

        String[][] headers = {
            {"Content-Type", "application/json", "Tells server/client what format the body is in."},
            {"Accept", "application/xml", "Tells server what format the client wants back."},
            {"Authorization", "Bearer abc123", "Carries authentication credentials."},
            {"Cache-Control", "no-cache", "Controls caching behavior."},
            {"User-Agent", "Java-HttpClient/17", "Identifies the client software."},
            {"Accept-Encoding", "gzip, deflate", "Tells server which compression formats client supports."}
        };

        for (String[] h : headers) {
            System.out.println("  " + h[0] + ": " + h[1]);
            System.out.println("    -> " + h[2]);
        }

        System.out.println("\n=== End of Http Battleground ===");
    }
}
