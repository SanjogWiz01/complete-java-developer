(function () {
    const minimumFare = 50;
    const perKm = 50;

    function toNumber(value, fallback) {
        const number = Number.parseFloat(value);
        return Number.isFinite(number) ? number : fallback;
    }

    function distanceKm(aLat, aLng, bLat, bLng) {
        const radius = 6371;
        const dLat = (bLat - aLat) * Math.PI / 180;
        const dLng = (bLng - aLng) * Math.PI / 180;
        const lat1 = aLat * Math.PI / 180;
        const lat2 = bLat * Math.PI / 180;
        const a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return radius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    function promoDiscount(subtotal, promoCode) {
        const code = (promoCode || "").trim().toUpperCase();
        if (code === "WELCOME10") {
            return subtotal * 0.1;
        }
        if (code === "CITY25") {
            return 25;
        }
        return 0;
    }

    function formatMoney(value) {
        return value.toFixed(2);
    }

    function initBookingMap() {
        const mapEl = document.getElementById("bookingMap");
        if (!mapEl || typeof L === "undefined") {
            return;
        }

        const pickupLatEl = document.getElementById("pickupLatitude");
        const pickupLngEl = document.getElementById("pickupLongitude");
        const dropLatEl = document.getElementById("dropoffLatitude");
        const dropLngEl = document.getElementById("dropoffLongitude");
        const pickupInput = document.getElementById("pickupLocation");
        const dropInput = document.getElementById("dropoffLocation");
        const vehicleSelect = document.getElementById("vehicleType");
        const promoInput = document.getElementById("promoCode");
        const rideTypeSelect = document.getElementById("rideType");
        const scheduledGroup = document.getElementById("scheduledPickupGroup");

        let activeStop = "pickup";
        const pickup = [
            toNumber(pickupLatEl.value, 28.2096),
            toNumber(pickupLngEl.value, 83.9856)
        ];
        const dropoff = [
            toNumber(dropLatEl.value, 28.2009),
            toNumber(dropLngEl.value, 83.9821)
        ];

        const map = L.map(mapEl).setView(pickup, 13);
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 19,
            attribution: "&copy; OpenStreetMap contributors"
        }).addTo(map);

        const pickupMarker = L.marker(pickup, { draggable: true }).addTo(map).bindPopup("Pickup");
        const dropoffMarker = L.marker(dropoff, { draggable: true }).addTo(map).bindPopup("Dropoff");
        let routeLine = L.polyline([pickup, dropoff], { color: "#0f766e", weight: 5 }).addTo(map);

        function setStop(type, latLng, label, forceLabel) {
            if (type === "pickup") {
                pickupLatEl.value = latLng.lat.toFixed(6);
                pickupLngEl.value = latLng.lng.toFixed(6);
                pickupMarker.setLatLng(latLng);
                if (label && pickupInput && (forceLabel || !pickupInput.value.trim())) {
                    pickupInput.value = label;
                }
            } else {
                dropLatEl.value = latLng.lat.toFixed(6);
                dropLngEl.value = latLng.lng.toFixed(6);
                dropoffMarker.setLatLng(latLng);
                if (label && dropInput && (forceLabel || !dropInput.value.trim())) {
                    dropInput.value = label;
                }
            }
            calculateEstimate();
        }

        function calculateEstimate() {
            const pLat = toNumber(pickupLatEl.value, pickup[0]);
            const pLng = toNumber(pickupLngEl.value, pickup[1]);
            const dLat = toNumber(dropLatEl.value, dropoff[0]);
            const dLng = toNumber(dropLngEl.value, dropoff[1]);
            const distance = distanceKm(pLat, pLng, dLat, dLng);
            const duration = Math.ceil((distance / 40) * 60);
            const subtotal = Math.max(minimumFare, distance * perKm);
            const discount = promoDiscount(subtotal, promoInput ? promoInput.value : "");
            const total = Math.max(minimumFare, subtotal - discount);

            document.querySelectorAll("[data-estimate-distance]").forEach((el) => {
                el.textContent = distance.toFixed(1);
            });
            document.querySelectorAll("[data-estimate-duration]").forEach((el) => {
                el.textContent = String(duration);
            });
            document.querySelectorAll("[data-estimate-discount]").forEach((el) => {
                el.textContent = formatMoney(discount);
            });
            document.querySelectorAll("[data-estimate-cost]").forEach((el) => {
                el.textContent = formatMoney(total);
            });

            routeLine.setLatLngs([[pLat, pLng], [dLat, dLng]]);
            map.fitBounds(routeLine.getBounds(), { padding: [28, 28] });
        }

        pickupInput && pickupInput.addEventListener("focus", () => activeStop = "pickup");
        dropInput && dropInput.addEventListener("focus", () => activeStop = "dropoff");
        pickupMarker.on("dragend", (event) => setStop("pickup", event.target.getLatLng()));
        dropoffMarker.on("dragend", (event) => setStop("dropoff", event.target.getLatLng()));
        map.on("click", (event) => {
            setStop(activeStop, event.latlng, "Pinned on map");
            activeStop = activeStop === "pickup" ? "dropoff" : "pickup";
        });

        document.querySelectorAll("[data-location-chip]").forEach((button) => {
            button.addEventListener("click", () => {
                const target = button.getAttribute("data-target-stop") || activeStop;
                const latLng = {
                    lat: toNumber(button.getAttribute("data-lat"), pickup[0]),
                    lng: toNumber(button.getAttribute("data-lng"), pickup[1])
                };
                setStop(target, latLng, button.getAttribute("data-label") || button.textContent.trim(), true);
            });
        });

        [vehicleSelect, promoInput].forEach((input) => {
            input && input.addEventListener("input", calculateEstimate);
        });

        function toggleSchedule() {
            if (!scheduledGroup || !rideTypeSelect) {
                return;
            }
            scheduledGroup.classList.toggle("d-none", rideTypeSelect.value !== "SCHEDULED");
        }

        rideTypeSelect && rideTypeSelect.addEventListener("change", toggleSchedule);
        toggleSchedule();
        calculateEstimate();
    }

    function initRouteMap() {
        const mapEl = document.getElementById("routeMap");
        if (!mapEl || typeof L === "undefined") {
            return;
        }

        const pickup = [
            toNumber(mapEl.dataset.pickupLat, 28.2096),
            toNumber(mapEl.dataset.pickupLng, 83.9856)
        ];
        const dropoff = [
            toNumber(mapEl.dataset.dropoffLat, 28.2009),
            toNumber(mapEl.dataset.dropoffLng, 83.9821)
        ];

        const map = L.map(mapEl).setView(pickup, 13);
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 19,
            attribution: "&copy; OpenStreetMap contributors"
        }).addTo(map);
        L.marker(pickup).addTo(map).bindPopup("Pickup");
        L.marker(dropoff).addTo(map).bindPopup("Dropoff");
        const routeLine = L.polyline([pickup, dropoff], { color: "#0f766e", weight: 5 }).addTo(map);
        map.fitBounds(routeLine.getBounds(), { padding: [28, 28] });
    }

    function initClientFilters() {
        document.querySelectorAll("[data-status-filter]").forEach((button) => {
            button.addEventListener("click", () => {
                const status = button.getAttribute("data-status-filter");
                document.querySelectorAll("[data-booking-status]").forEach((item) => {
                    const matches = status === "ALL" || item.getAttribute("data-booking-status") === status;
                    item.classList.toggle("d-none", !matches);
                });
                document.querySelectorAll("[data-status-filter]").forEach((control) => {
                    control.classList.toggle("active", control === button);
                });
            });
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        initBookingMap();
        initRouteMap();
        initClientFilters();
    });
})();
