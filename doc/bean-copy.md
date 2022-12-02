```java
package com.gtt.bean.copy;

import lombok.Data;
import java.util.List;
import org.springframework.beans.BeanUtils;
import java.util.Arrays;


@Data
public class A {
    private String name;

    private List<Integer> ids;
}
@Data
public class B {
    private String name;

    private List<String> ids;
}

public class BeanUtilDemo {
    public static void main(String[] args) {
        A first = new A();
        first.setName("demo");
        first.setIds(Arrays.asList(1, 2, 3));

        B second = new B();
        BeanUtils.copyProperties(first, second);
        for (String each : second.getIds()) {// 类型转换异常
            System.out.println(each);
        }
    }
}
```
运行上述示例时，会发生类型转换异常。

接下来我们看下 mapstruct：
```java
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.Arrays;

@Mapper
public interface Converter {
    Converter INSTANCE = Mappers.getMapper(Converter.class);

    B aToB(A car);
}


public class BeanUtilDemo {
    public static void main(String[] args) {
        A first = new A();
        first.setName("demo");
        first.setIds(Arrays.asList(1, 2, 3));

        B second = Converter.INSTANCE.aToB(first);
        for (String each : second.getIds()) {// 正常
            System.out.println(each);
        }
    }
}
```
看下编译生成的 Converter 实现类：
```java
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
@Component
public class ConverterImpl implements Converter {

    @Override
    public B aToB(A car) {
        if ( car == null ) {
            return null;
        }

        B b = new B();

        b.setName( car.getName() );
        b.setIds( integerListToStringList( car.getIds() ) );

        return b;
    }

    protected List<String> integerListToStringList(List<Integer> list) {
        if ( list == null ) {
            return null;
        }

        List<String> list1 = new ArrayList<String>( list.size() );
        for ( Integer integer : list ) {
            //自动帮我们进行了转换，我们可能没有意识到类型并不一致。
            list1.add( String.valueOf( integer ) );
        }

        return list1;
    }
}
```
如果我们在 A 类中添加一个 String number 属性，
在 B 类中添加一个 Long number 属性，
使用 mapstruect 当 string number 设置为非数字类型时就会报 .NumberFormatException。



由于 Java 的泛型其实是编译期检查，编译后泛型擦除，导致运行时 List<Integer> 和 List<String> 都是 List 类型，可以正常赋值。这就导致在使用很多属性映射工具时，编译时不容易明显的错误。

mapstruct 自定义了注解处理器，在编译阶段可以读取映射双方的泛型类型，进而进行映射。但是这种映射也很可怕，有时候我们由于粗心等原因定义错了类型，自动帮助我们进行了转换，会带了很多副作用。
由于 Java 的泛型其实是编译期检查，编译后泛型擦除，导致运行时 List<Integer> 和 List<String> 都是 List 类型，可以正常赋值。这就导致在使用很多属性映射工具时，编译时不容易明显的错误。

mapstruct 自定义了注解处理器，在编译阶段可以读取映射双方的泛型类型，进而进行映射。但是这种映射也很可怕，有时候我们由于粗心等原因定义错了类型，自动帮助我们进行了转换，会带了很多副作用。
