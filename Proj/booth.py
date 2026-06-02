def booth_multiplication(m, r, n):
    """
    Booth's Algorithm for signed binary multiplication.
    m = multiplicand (integer)
    r = multiplier (integer)
    n = number of bits
    """
    # Convert to binary with n bits
    m_bin = format(m if m >= 0 else (1 << n) + m, f'0{n}b')
    r_bin = format(r if r >= 0 else (1 << n) + r, f'0{n}b')

    # Registers
    A = m << n
    S = ((-m) & ((1 << n) - 1)) << n
    P = (r & ((1 << n) - 1)) | (0 << n)  # multiplier + extra bit

    # Mask for 2n+1 bits
    mask = (1 << (2 * n + 1)) - 1

    print(f"Initial P: {P:0{2*n+1}b}")

    for i in range(n):
        if P & 1 == 1 and (P >> 1) & 1 == 0:  # 01 → add A
            P = (P + A) & mask
        elif P & 1 == 0 and (P >> 1) & 1 == 1:  # 10 → add S
            P = (P + S) & mask

        # Arithmetic right shift
        P = (P >> 1) | (P & (1 << (2 * n)))  # preserve sign bit

        print(f"Step {i+1}: {P:0{2*n+1}b}")

    # Final product
    product = (P >> 1) & ((1 << (2 * n)) - 1)
    # Convert back to signed integer
    if product & (1 << (2 * n - 1)):
        product -= (1 << (2 * n))

    return product


# Example usage
m = -3
r = 7
n = 4  # number of bits
result = booth_multiplication(m, r, n)
print(f"{m} * {r} = {result}")
