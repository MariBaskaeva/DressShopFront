function changeColor(color) {
    window.location.replace(window.location.origin + window.location.pathname + '?color=' + color.value);
}

function changeSize(size) {
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
    window.location.replace(window.location.origin + window.location.pathname + '?color=' + params.color + '&size=' + size.value );
}