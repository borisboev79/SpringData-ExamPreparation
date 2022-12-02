package hiberspring.service.impl;

import hiberspring.domain.dtos.ProductDTO;
import hiberspring.domain.dtos.wrappers.ProductsWrapperDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FileUtil fileReader;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository, FileUtil fileReader, XmlParser parser, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.fileReader = fileReader;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public Boolean productsAreImported() {
        return productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.fileReader.readFile(PATH_TO_FILES + "products.xml");
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
        ProductsWrapperDTO productsDto = parser.parseXml(ProductsWrapperDTO.class, PATH_TO_FILES + "products.xml");


        for (ProductDTO productDto : productsDto.getProducts()) {

            boolean isValid = this.validator.isValid(productDto);

            if (this.productRepository.findFirstByName(productDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Product product = this.mapper.map(productDto, Product.class);

                Branch branch = this.branchRepository.findFirstByName(productDto.getBranch()).get();

                product.setBranch(branch);

                this.productRepository.save(product);

                result.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        product.getClass().getSimpleName(),
                        productDto.getName()));
                result.append(System.lineSeparator());

            } else {
                result.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
