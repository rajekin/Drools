public List<Product> removeProductByIdAndName(List<Product> products, int id, String name) {
        List<Product> removedProducts = new ArrayList<>();
        Iterator<Product> iterator = products.iterator();
        
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId() == id && product.getName().equals(name)) {
                removedProducts.add(product);
                iterator.remove();
            }
        }

        return removedProducts;
    }
