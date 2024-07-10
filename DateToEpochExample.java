products.stream()
                .filter(product -> !(product.getId() == id && product.getName().equals(name)))
                .collect(Collectors.toList());
