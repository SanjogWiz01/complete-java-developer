(function () {
    const minimumFare = 50;
    const perKm = 50;
    const routeDebounceMs = 220;
    const knownStops = [
        { aliases: ["lakeside", "lakeside, pokhara"], label: "Lakeside, Pokhara", lat: 28.2096, lng: 83.9856 },
        { aliases: ["phewa lake", "phewa lake, pokhara"], label: "Phewa Lake, Pokhara", lat: 28.2143, lng: 83.9576 },
        { aliases: ["airport", "pokhara international airport"], label: "Pokhara International Airport", lat: 28.2009, lng: 83.9821 },
        { aliases: ["sarangkot", "sarangkot, pokhara"], label: "Sarangkot, Pokhara", lat: 28.2439, lng: 83.9483 },
        { aliases: ["bindhyabasini", "bindhyabasini temple"], label: "Bindhyabasini Temple, Pokhara", lat: 28.2333, lng: 83.9833 },
        { aliases: ["prithvi chowk", "prithvi chowk, pokhara"], label: "Prithvi Chowk, Pokhara", lat: 28.2130, lng: 83.9973 }
    ];
    const rideModeProfiles = {
        BALANCED: { label: "Smart Balance", fare: 1.00, duration: 1.00, assurance: 74, eco: 0 },
        FASTEST: { label: "Fastest ETA", fare: 1.12, duration: 0.88, assurance: 78, eco: 0 },
        ECONOMY: { label: "Economy", fare: 0.93, duration: 1.06, assurance: 70, eco: 0.04 },
        SAFETY: { label: "Safety+", fare: 1.06, duration: 1.04, assurance: 88, eco: 0.02 },
        ECO: { label: "Eco Ride", fare: 0.97, duration: 1.08, assurance: 76, eco: 0.16 }
    };

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
        if (code === "SHARE15") {
            return subtotal * 0.15;
        }
        return 0;
    }

    function formatMoney(value) {
        return Number(value || 0).toFixed(2);
    }

    function setText(selector, value) {
        document.querySelectorAll(selector).forEach((el) => {
            el.textContent = value;
        });
    }

    function selectedRideMode() {
        const selected = document.querySelector("input[name='rideMode']:checked");
        return selected ? selected.value : "BALANCED";
    }

    function rideModeProfile(value) {
        return rideModeProfiles[(value || "BALANCED").toUpperCase()] || rideModeProfiles.BALANCED;
    }

    function lookupStop(label) {
        const normalized = (label || "").toLowerCase().replace(/,/g, " ").replace(/\s+/g, " ").trim();
        return knownStops.find((stop) => stop.aliases.some((alias) => normalized.includes(alias) || alias.includes(normalized)));
    }

    function buildRoutePreviewUrl(values) {
        const params = new URLSearchParams(values);
        return `/api/routes/preview?${params.toString()}`;
    }

    function validCoordinate(point) {
        return Array.isArray(point)
            && point.length === 2
            && Number.isFinite(point[0])
            && Number.isFinite(point[1])
            && point[0] >= 27.8
            && point[0] <= 28.6
            && point[1] >= 83.5
            && point[1] <= 84.4;
    }

    function safeRouteCoordinates(rawCoordinates, fallback) {
        const coordinates = (rawCoordinates || []).map((point) => [Number(point[0]), Number(point[1])]);
        const filtered = coordinates.filter(validCoordinate);
        return filtered.length > 1 ? filtered : fallback;
    }

    function fitRoute(map, routeLine, coordinates) {
        routeLine.setLatLngs(coordinates);
        const bounds = routeLine.getBounds();
        if (bounds && bounds.isValid()) {
            map.fitBounds(bounds.pad(0.18), {
                animate: true,
                duration: 0.35,
                paddingTopLeft: [34, 34],
                paddingBottomRight: [34, 34],
                maxZoom: 15
            });
        }
    }

    function createStopIcon(type) {
        const icon = type === "pickup" ? "fa-location-dot" : "fa-flag-checkered";
        const label = type === "pickup" ? "P" : "D";
        return L.divIcon({
            className: `route-marker route-marker-${type}`,
            html: `<span><i class="fa-solid ${icon}"></i></span><strong>${label}</strong>`,
            iconSize: [42, 42],
            iconAnchor: [21, 38],
            popupAnchor: [0, -34]
        });
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
        const passengerInput = document.getElementById("passengerCount");
        const offlineToggle = document.getElementById("offlineNavigationEnabled");
        const rideTypeSelect = document.getElementById("rideType");
        const rideModeInputs = Array.from(document.querySelectorAll("input[name='rideMode']"));
        const scheduledGroup = document.getElementById("scheduledPickupGroup");
        const instructionList = document.querySelector("[data-route-instructions]");
        const driverMatch = document.querySelector("[data-driver-match]");
        const routeEngine = document.querySelector("[data-route-engine]");
        const routeCache = document.querySelector("[data-route-cache]");
        const modeLabel = document.querySelector("[data-mode-label]");
        const matchConfidence = document.querySelector("[data-match-confidence]");
        const ecoSavings = document.querySelector("[data-eco-savings]");

        let activeStop = "pickup";
        let routeTimer = null;
        let lastRouteKey = "";

        const pickup = [
            toNumber(pickupLatEl.value, 28.2096),
            toNumber(pickupLngEl.value, 83.9856)
        ];
        const dropoff = [
            toNumber(dropLatEl.value, 28.2009),
            toNumber(dropLngEl.value, 83.9821)
        ];

        const pokharaBounds = L.latLngBounds([27.8, 83.5], [28.6, 84.4]);
        const map = L.map(mapEl, {
            maxBounds: pokharaBounds,
            maxBoundsViscosity: 0.65,
            zoomControl: false
        }).setView(pickup, 13);
        L.control.zoom({ position: "bottomright" }).addTo(map);
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 19,
            noWrap: true,
            attribution: "&copy; OpenStreetMap contributors"
        }).addTo(map);

        const pickupMarker = L.marker(pickup, { draggable: true, icon: createStopIcon("pickup") }).addTo(map).bindPopup("Pickup");
        const dropoffMarker = L.marker(dropoff, { draggable: true, icon: createStopIcon("dropoff") }).addTo(map).bindPopup("Dropoff");
        const routeHalo = L.polyline([pickup, dropoff], {
            color: "#ffffff",
            weight: 11,
            opacity: 0.95,
            lineCap: "round",
            lineJoin: "round"
        }).addTo(map);
        let routeLine = L.polyline([pickup, dropoff], {
            color: "#1d4ed8",
            weight: 6,
            opacity: 0.96,
            lineCap: "round",
            lineJoin: "round"
        }).addTo(map);

        function currentValues() {
            return {
                pickupLocation: pickupInput ? pickupInput.value : "Pickup",
                dropoffLocation: dropInput ? dropInput.value : "Dropoff",
                pickupLatitude: pickupLatEl.value,
                pickupLongitude: pickupLngEl.value,
                dropoffLatitude: dropLatEl.value,
                dropoffLongitude: dropLngEl.value,
                vehicleType: vehicleSelect ? vehicleSelect.value : "SEDAN",
                promoCode: promoInput ? promoInput.value : "",
                passengerCount: passengerInput ? passengerInput.value : "1",
                rideMode: selectedRideMode(),
                offlineNavigationEnabled: offlineToggle ? String(offlineToggle.checked) : "true"
            };
        }

        function routeKey(values) {
            return [
                Number(values.pickupLatitude).toFixed(4),
                Number(values.pickupLongitude).toFixed(4),
                Number(values.dropoffLatitude).toFixed(4),
                Number(values.dropoffLongitude).toFixed(4),
                values.vehicleType,
                values.rideMode,
                values.promoCode.trim().toUpperCase(),
                values.passengerCount,
                values.offlineNavigationEnabled
            ].join("|");
        }

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
            renderFallbackEstimate();
            scheduleRoutePreview();
        }

        function renderFallbackEstimate() {
            const pLat = toNumber(pickupLatEl.value, pickup[0]);
            const pLng = toNumber(pickupLngEl.value, pickup[1]);
            const dLat = toNumber(dropLatEl.value, dropoff[0]);
            const dLng = toNumber(dropLngEl.value, dropoff[1]);
            const distance = distanceKm(pLat, pLng, dLat, dLng);
            const mode = rideModeProfile(selectedRideMode());
            const duration = Math.max(1, Math.ceil(((distance / 40) * 60) * mode.duration));
            const subtotal = Math.max(minimumFare, distance * perKm) * mode.fare;
            const discount = promoDiscount(subtotal, promoInput ? promoInput.value : "");
            const total = Math.max(minimumFare, subtotal - discount);

            setText("[data-estimate-distance]", distance.toFixed(1));
            setText("[data-estimate-duration]", String(duration));
            setText("[data-estimate-discount]", formatMoney(discount));
            setText("[data-estimate-cost]", formatMoney(total));
            setText("[data-demand-label]", "Calculating");
            setText("[data-demand-multiplier]", "");
            if (modeLabel) {
                modeLabel.textContent = mode.label;
            }
            if (matchConfidence) {
                matchConfidence.textContent = `${mode.assurance}%`;
            }
            if (ecoSavings) {
                ecoSavings.textContent = formatMoney(distance * mode.eco);
            }

            const fallbackRoute = [[pLat, pLng], [dLat, dLng]];
            fitRoute(map, routeHalo, fallbackRoute);
            fitRoute(map, routeLine, fallbackRoute);
        }

        function scheduleRoutePreview(force) {
            const values = currentValues();
            const nextKey = routeKey(values);
            if (!force && nextKey === lastRouteKey) {
                return;
            }
            lastRouteKey = nextKey;
            clearTimeout(routeTimer);
            routeTimer = setTimeout(() => fetchRoutePreview(values), routeDebounceMs);
        }

        function fetchRoutePreview(values) {
            fetch(buildRoutePreviewUrl(values), { headers: { "Accept": "application/json" } })
                .then((response) => response.ok ? response.json() : Promise.reject(new Error("Route preview failed")))
                .then(renderRoutePreview)
                .catch(() => {
                    if (routeCache) {
                        routeCache.textContent = "Fallback";
                    }
                });
        }

        function renderRoutePreview(preview) {
            setText("[data-estimate-distance]", Number(preview.distanceKm).toFixed(1));
            setText("[data-estimate-duration]", String(preview.durationMinutes));
            setText("[data-estimate-discount]", formatMoney(preview.promoDiscount));
            setText("[data-estimate-cost]", formatMoney(preview.estimatedFare));
            setText("[data-demand-label]", preview.demandLabel || "Balanced");
            setText("[data-demand-multiplier]", `x${Number(preview.demandMultiplier || 1).toFixed(2)}`);
            if (modeLabel) {
                modeLabel.textContent = preview.rideModeLabel || rideModeProfile(preview.rideMode).label;
            }
            if (matchConfidence) {
                matchConfidence.textContent = `${Math.round(Number(preview.matchConfidenceScore || 0.74) * 100)}%`;
            }
            if (ecoSavings) {
                ecoSavings.textContent = formatMoney(preview.ecoSavingsKg);
            }

            if (driverMatch) {
                const bestDriver = (preview.driverCandidates || [])[0];
                driverMatch.textContent = bestDriver
                    ? `${bestDriver.driverName} in ${bestDriver.etaMinutes} min`
                    : "No available driver";
            }
            if (routeEngine) {
                routeEngine.textContent = preview.routeSummary || preview.algorithm || "Shortest route";
            }
            if (routeCache) {
                routeCache.textContent = preview.cacheHit ? "Cached" : "Live";
            }
            if (instructionList) {
                instructionList.innerHTML = "";
                (preview.instructions || []).forEach((instruction) => {
                    const item = document.createElement("li");
                    item.textContent = instruction.maneuver;
                    instructionList.appendChild(item);
                });
            }

            const fallbackRoute = [
                [toNumber(pickupLatEl.value, pickup[0]), toNumber(pickupLngEl.value, pickup[1])],
                [toNumber(dropLatEl.value, dropoff[0]), toNumber(dropLngEl.value, dropoff[1])]
            ];
            const coordinates = safeRouteCoordinates(preview.routeCoordinates, fallbackRoute);
            if (coordinates.length > 1) {
                fitRoute(map, routeHalo, coordinates);
                fitRoute(map, routeLine, coordinates);
            }
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

        document.querySelectorAll("[data-route-suggestion]").forEach((button) => {
            button.addEventListener("click", () => {
                const parts = (button.getAttribute("data-route") || "").split(/\s+to\s+/i);
                if (parts.length !== 2) {
                    return;
                }
                const start = lookupStop(parts[0]);
                const end = lookupStop(parts[1]);
                if (start) {
                    setStop("pickup", { lat: start.lat, lng: start.lng }, start.label, true);
                }
                if (end) {
                    setStop("dropoff", { lat: end.lat, lng: end.lng }, end.label, true);
                }
            });
        });

        function syncRideModeCards() {
            rideModeInputs.forEach((input) => {
                const card = input.closest(".ride-mode-card");
                if (card) {
                    card.classList.toggle("active", input.checked);
                }
            });
        }

        rideModeInputs.forEach((input) => {
            input.addEventListener("change", () => {
                syncRideModeCards();
                renderFallbackEstimate();
                scheduleRoutePreview(true);
            });
        });

        [vehicleSelect, promoInput, passengerInput, offlineToggle].forEach((input) => {
            input && input.addEventListener("input", () => {
                renderFallbackEstimate();
                scheduleRoutePreview();
            });
            input && input.addEventListener("change", () => {
                renderFallbackEstimate();
                scheduleRoutePreview(true);
            });
        });

        function toggleSchedule() {
            if (!scheduledGroup || !rideTypeSelect) {
                return;
            }
            scheduledGroup.classList.toggle("d-none", rideTypeSelect.value !== "SCHEDULED");
        }

        rideTypeSelect && rideTypeSelect.addEventListener("change", toggleSchedule);
        syncRideModeCards();
        toggleSchedule();
        renderFallbackEstimate();
        scheduleRoutePreview(true);
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

        const pokharaBounds = L.latLngBounds([27.8, 83.5], [28.6, 84.4]);
        const map = L.map(mapEl, {
            maxBounds: pokharaBounds,
            maxBoundsViscosity: 0.65,
            zoomControl: false
        }).setView(pickup, 13);
        L.control.zoom({ position: "bottomright" }).addTo(map);
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            maxZoom: 19,
            noWrap: true,
            attribution: "&copy; OpenStreetMap contributors"
        }).addTo(map);
        L.marker(pickup, { icon: createStopIcon("pickup") }).addTo(map).bindPopup("Pickup");
        L.marker(dropoff, { icon: createStopIcon("dropoff") }).addTo(map).bindPopup("Dropoff");
        const routeHalo = L.polyline([pickup, dropoff], {
            color: "#ffffff",
            weight: 11,
            opacity: 0.95,
            lineCap: "round",
            lineJoin: "round"
        }).addTo(map);
        const routeLine = L.polyline([pickup, dropoff], {
            color: "#1d4ed8",
            weight: 6,
            opacity: 0.96,
            lineCap: "round",
            lineJoin: "round"
        }).addTo(map);
        fitRoute(map, routeHalo, [pickup, dropoff]);
        fitRoute(map, routeLine, [pickup, dropoff]);

        const values = {
            pickupLocation: mapEl.dataset.pickupLabel || "Pickup",
            dropoffLocation: mapEl.dataset.dropoffLabel || "Dropoff",
            pickupLatitude: String(pickup[0]),
            pickupLongitude: String(pickup[1]),
            dropoffLatitude: String(dropoff[0]),
            dropoffLongitude: String(dropoff[1]),
            vehicleType: mapEl.dataset.vehicleType || "SEDAN",
            promoCode: mapEl.dataset.promoCode || "",
            passengerCount: mapEl.dataset.passengerCount || "1",
            rideMode: mapEl.dataset.rideMode || "BALANCED",
            offlineNavigationEnabled: mapEl.dataset.offlineEnabled || "true"
        };

        fetch(buildRoutePreviewUrl(values), { headers: { "Accept": "application/json" } })
            .then((response) => response.ok ? response.json() : null)
            .then((preview) => {
                if (!preview || !preview.routeCoordinates) {
                    return;
                }
                const coordinates = safeRouteCoordinates(preview.routeCoordinates, [pickup, dropoff]);
                if (coordinates.length > 1) {
                    fitRoute(map, routeHalo, coordinates);
                    fitRoute(map, routeLine, coordinates);
                }
            })
            .catch(() => {});
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
